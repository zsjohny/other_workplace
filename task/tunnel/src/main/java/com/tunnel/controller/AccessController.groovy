package com.tunnel.controller

import com.tunnel.domain.*
import com.tunnel.service.BaseDomainServer
import com.tunnel.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpSession
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by nessary on 16-10-10.
 */
@Controller
@RequestMapping("/login")
public class AccessController {
    @Autowired
    private BaseDomainServer baseDomainServer;

    @Autowired
    private DownloadExcel downloadExcel

    //超级管理员
    private String SUPER_ADMIN = "admin"

    private String REDIRECT = "redirect:/login"
    private String FORWARD = "FORWARD:/login"

    private Logger logger = LoggerFactory.getLogger(AccessController.class)


    class ResultTips implements Serializable {
        Boolean flag
        String msg = ""
        String jessionId
    }

    //登录
    @ResponseBody
    @RequestMapping("/load")
    public ResultTips load(HttpServletRequest request, String name
                           , String password) {


        ResultTips resultTips = new ResultTips()


        Boolean flag = false;

        String sessionId = CreatDatabase.getSessionId(request)



        if (StringUtils.isEmpty(sessionId)) {
            //以防万一默认生成
            sessionId = UUID.randomUUID().toString()
        }
        try {

            logger.info("用户{},开始访问登录页面", name)
            User user = baseDomainServer.findBaseDomainByBaseDomain(new User("name": name))

            if (user != null) {

                //判断是否正常还是禁用
                if (!user.getStatus()) {
                    resultTips.setMsg("账号:" + user.getName() + "已经停用,请联系管理员!!")
                } else {
                    String correctPass = DesUtil.decrypt(user.getPassword(), user.getPassKey())
                    if (correctPass.equals(password)) {

                        flag = true
                        HttpSession session = request.getSession()
                        session.setAttribute(sessionId, user)
                        resultTips.setJessionId(sessionId)
                        logger.info("用户{},正确访问登录页面", name)
                        //修改登录时间
                        baseDomainServer.updateBaseDomainByBaseDomain(new User("name": name, "loadTime": new Date()))
                    }
                }


            }
            logger.info("用户{},结束访问登录页面", name)

        } catch (Exception e) {
            logger.warn("用户{},访问登录页面出错", name, e)
        }

        resultTips.setFlag(flag)
        return resultTips

    }

    //访问
    @RequestMapping("/access")
    public String access(HttpServletRequest request,
                         @RequestParam(value = "sessionId") String jSessionId, ModelMap map) {


        String sessionId = CreatDatabase.getSessionId(request)


        if (!sessionId.equals(jSessionId)) {

            //替换
            if (request.getSession().getAttribute(jSessionId) != null) {
                User user = request.getSession().getAttribute(jSessionId)
                request.getSession().setAttribute(sessionId, user)
                request.getSession().removeAttribute(jSessionId)
                logger.info("原先sessionId={},替换成功后的sessionId={}", jSessionId, sessionId)
            }

        }



        if (StringUtils.isEmpty(sessionId)) {
            return "index"
        }
        User user = request.getSession().getAttribute(sessionId)
        try {
            logger.info("用户{},开始访问后台页面", user == null ? sessionId : user.getName())
            if (user != null) {


                if (SUPER_ADMIN.equals(user.getName())) {
                    map.addAttribute("author", User.UserType.superUser.getValue())
                } else {

                    int authorType = User.UserType.seniorUser.getValue()
                    if (user.getAuthorLevel() == User.UserType.commontUser.getValue()) {
                        authorType = User.UserType.commontUser.getValue()
                    }

                    map.addAttribute("author", authorType)

                }
                map.addAttribute("name", user.getName())
                logger.info("用户{},正确访问后台页面", user == null ? sessionId : user.getName())

                return "login"
            }
            logger.info("用户{},结束访问后台页面", user == null ? sessionId : user.getName())

        } catch (Exception e) {
            logger.warn("用户{},访问登录页面出错", user == null ? sessionId : user.getName(), e)
        }

        return "index"

    }

    private static ConcurrentHashMap<String, Integer> onlineMap = new ConcurrentHashMap<>()

    //访问
    @RequestMapping("/againMenu")
    public String againMenu(String path,
                            @RequestParam(value = "params", required = false) String params, ModelMap map, HttpServletRequest request) {


        if (StringUtils.isEmpty(path) || !path.contains("/")) {
            return "index"
        }


        if (!StringUtils.isEmpty(params)) {
            map.put("params", params)
            //测试--------
            try {
                User user = request.getSession().getAttribute(CreatDatabase.getSessionId(request))
                map.addAttribute("author", user.getAuthorLevel())
                map.put("pageName", false)

            } catch (Exception e) {

            }
        }


        return path

    }

    //添加用户
    @ResponseBody
    @RequestMapping("/modifyUser")
    public Integer modifyUser(HttpServletRequest request, User user) {

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return 1
        }
        User userOnline = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问添加用户页面", userOnline == null ? sessionId : userOnline.getName())

        try {

            if (SUPER_ADMIN.equals(userOnline.getName())) {

                //检查要修改的Id是否是超级管理员
                if (user.getId().equals(userOnline.getId())) {
                    user.setName(SUPER_ADMIN)
                }

                //检查用户名
                User userFind = baseDomainServer.findBaseDomainByBaseDomain(new User("name": user.getName()))



                if (user.getId() != null) {


                    if (userFind != null && !user.getId().equals(userFind.getId())) {
                        logger.info("用户{}已经存在,无需添加", userFind.getName())
                        return 2


                    }
                    String key = String.valueOf(UUID.randomUUID())

                    if (StringUtils.isEmpty(user.getPassword())) {
                        key = user.getPassKey()
                        user.setPassword(user.getOriginalPass())
                    }
                    user.setPassword(DesUtil.encrypt(user.getPassword().toLowerCase(), key))
                    user.setPassKey(key)

                    //存在继续修改
                    baseDomainServer.updateBaseDomainByBaseDomain(user)

                    logger.info("用户{},修改用户{}页面成功", userOnline == null ? sessionId : userOnline.getName(), user.getName())
                    return 0


                } else {

                    if (userFind != null) {
                        logger.info("用户{}已经存在,无需添加", userFind.getName())
                        return 2
                    }
                    user.setCreateTime(LocalDateTime.now())
                    String key = String.valueOf(UUID.randomUUID())
                    user.setPassword(DesUtil.encrypt(user.getPassword().toLowerCase(), key))
                    user.setPassKey(key)
                    baseDomainServer.saveBaseDomain(user)
                    logger.info("用户{},添加用户页面成功", userOnline == null ? sessionId : userOnline.getName())
                    return 0

                }
            }

        } catch (Exception e) {
            logger.warn("用户{},访问添加用户页面出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return 1

    }

    //查找用户
    @RequestMapping("/findUser")
    public String findUser(HttpServletRequest request, @RequestParam(value = "page", required = false) Integer page
                           ,
                           @RequestParam(value = "id", required = false) Integer id,
                           @RequestParam(value = "name", required = false) String name, ModelMap map) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return "index"
        }

        User userOnline = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问查找用户页面", userOnline == null ? sessionId : userOnline.getName())

        try {

            if (SUPER_ADMIN.equals(userOnline.getName())) {

                name = StringUtils.isEmpty(name) ? "" : name
                map.put("name", name)
                if (id != null && page == null) {
                    User userFind = baseDomainServer.findBaseDomainByBaseDomain(new User("id": id))

                    if (userFind == null) {
                        page = 1

                    } else {

                        userFind.setPassword(DesUtil.decrypt(userFind.getPassword(), userFind.getPassKey()))

                        map.put("user", userFind)

                        return "user/edit"
                    }

                }

                if (page <= 0 || page == null) {
                    page = 1
                }

                Integer start = (page - 1) * BaseDomain.pageSize

                List<User> list = baseDomainServer.findBaseDomainALlByPage(new User("page": start, "name": name))

                if (list == null || list.isEmpty()) {
                    logger.info("没有用户数据!!")
                    map.put("list", new ArrayList())

                    return REDIRECT + "/againMenu.do?path=user/index"
                }
                map.put("list", list)
                map.put("page", page)
                Integer count = baseDomainServer.findBaseDomainCountByBaseDomain(new User("name": name))
                map.put("total", count)
                map.put("totalPage", (int) (Math.ceil(count / BaseDomain.pageSize)))
                logger.info("用户{},结束访问所有用户页面", userOnline == null ? sessionId : userOnline.getName())
                return "user/index"
            }


        } catch (Exception e) {
            logger.warn("用户{},访问所有用户出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return "index"

    }

    //查找用户
    @RequestMapping("/removerUser")
    public String removerUser(HttpServletRequest request, Integer id, Integer page, String name) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return REDIRECT + "/findUser.do?page=" + page
        }

        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问删除用户页面", user == null ? sessionId : user.getName())

        try {

            if (SUPER_ADMIN.equals(user.getName())) {

                User userFindById = baseDomainServer.findBaseDomainByBaseDomain(new User("id": id))

                if (userFindById == null) {
                    logger.warn("查找用户数据为空")
                    return REDIRECT + "/findUser.do?page=" + page

                }
                if (user.getId().equals(id)) {
                    logger.warn("不能删除自己")
                    return REDIRECT + "/findUser.do?page=" + page
                }
                baseDomainServer.deleteBaseDomainById(new User("id": id))
                logger.info("用户{},结束删除所有用户页面", user == null ? sessionId : user.getName())
                return REDIRECT + "/findUser.do?page=" + page + "&name=" + name
            }


        } catch (Exception e) {
            logger.warn("用户{},删除所有用户出错", user == null ? sessionId : user.getName(), e)
        }
        return REDIRECT + "/findUser.do?page=" + page
    }

    //用户退出
    @RequestMapping("/quitUser")
    public String quitUser(HttpServletRequest request) {
        String sessionId = CreatDatabase.getSessionId(request)

        try {
            logger.info("用户sessionId={} 开始退出管理页面", sessionId)
            if (!StringUtils.isEmpty(sessionId)) {
                User user = request.getSession().getAttribute(sessionId)
                request.getSession().removeAttribute(sessionId)
                //去除对应缓存的下载
                DownloadExcel.downloadExcelType.get(sessionId).clear()

                logger.info("用户{},sessionId={} 退出管理页面", user == null ? sessionId : user.getName(), sessionId)
            }

        } catch (Exception e) {

            logger.info("用户{},退出管理页面失败", sessionId, e)
        }
        return "index"
    }

    //下载excel
    @RequestMapping("/downloadExcel")
    public ResponseEntity<byte[]> downloadExcel(ClassType classType, Integer downloadType, HttpServletRequest request) {

        String sessionId = CreatDatabase.getSessionId(request)

        HttpHeaders headers = new HttpHeaders();
        // 设定格式
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        headers.setContentDispositionFormData("attachment",
                new String(((StringUtils.isEmpty(classType.getExcelName()) ? "example" : classType.getExcelName()) + ".xls").getBytes("utf-8"), "iso8859-1"));

        if (StringUtils.isEmpty(sessionId) || downloadType == null || DownloadExcel.downloadExcelType.get(sessionId) == null || (!"total".equals(classType.getClassName()) && DownloadExcel.downloadExcelType.get(sessionId).get(classType.getClassName()) == null)) {

            return new ResponseEntity<byte[]>(Files.readAllBytes(Paths.get(new File(AccessController.class.getClassLoader().getResource("example.xls").getFile()).getPath())), headers, HttpStatus.OK);

        }

        try {
            logger.info("用户sessionId={} 开始下载excel{}页面", sessionId, classType.getClassName())


            BaseDomain baseDomain = null

            if (!StringUtils.isEmpty(classType.getTunnelNumber())) {


                if (!"total".equals(classType.getClassName())) {

                    classType.setTunnelNumber(classType.getTunnelNumber().split(":")[1])
                }


                switch (classType.getClassName()) {
                //下载总的部分
                    case "total":
                        baseDomain = new User("passKey": classType.getTunnelNumber())
                        break
                    case "tunnelInfo":
                        baseDomain = new TunnelInfo("page": 0)
                        break
                    case "project":
                        baseDomain = new Project("page": 0)
                        break
                    case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
                        baseDomain = new TunnelImportPort("tunnelNumber": classType.getTunnelNumber(), "page": 0)
                        break;
                    case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():

                        baseDomain = new TunnelShallowCover("tunnelNumber": classType.getTunnelNumber(), "page": 0)
                        break;
                    case ClassType.ClassTypeMapping.assistTunnel.getKey():

                        baseDomain = new AssistTunnel("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():

                        baseDomain = new TunnelGrabageExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)
                        break;

                    case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
                        baseDomain = new TunnelHeadRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;

                    case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():

                        baseDomain = new OverBreakRiskExmaine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():

                        baseDomain = new SurgeMudRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():


                        baseDomain = new ShapeRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
                        baseDomain = new RockOutburstRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.gasRiskExamine.getKey():

                        baseDomain = new GasRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;

                    case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():

                        baseDomain = new FireRiskExmaine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;

                    case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():

                        baseDomain = new TrafficAccidentRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)


                        break;
                    case ClassType.ClassTypeMapping.envirRiskExamine.getKey():

                        baseDomain = new EnvirRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;

                    case ClassType.ClassTypeMapping.otherRiskExamine.getKey():

                        baseDomain = new OtherRiskExamine("tunnelNumber": classType.getTunnelNumber(), "page": 0)

                        break;
                    case ClassType.ClassTypeMapping.figure.getKey():
                        baseDomain = new Figure("tunnelNumber": classType.getTunnelNumber(), "page": 0)


                }

            }


            logger.info("用户sessionId={} 结束下载excel{}页面", sessionId, classType.getClassName())


            return new ResponseEntity<byte[]>(downloadExcel.getDownloadExcel(sessionId, downloadType, classType.getClassName(), baseDomain), headers, HttpStatus.OK);


        } catch (Exception e) {

            logger.warn("用户sessionId={} 下载excel页面失败", sessionId, e)
        }


    }

    //上传Excel
    @ResponseBody
    @RequestMapping("/uploadExcel")
    public Boolean uploadPhotos(HttpServletRequest request,
                                @RequestParam("file") MultipartFile file) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId) || file == null || file.isEmpty()) {
            return false


        }


        User user = request.getSession().getAttribute(sessionId)


        try {
            logger.info("用户{},开始上传Excel", user == null ? sessionId : user.getName())

            File tempFile = File.createTempFile("start", "end")
            file.transferTo(tempFile)
            downloadExcel.setDownoadExcel(tempFile)

            logger.info("用户{},结束上传Excel", user == null ? sessionId : user.getName())

            return true
        } catch (Exception e) {

            logger.warn("用户{},上传上传Excel失败", user == null ? sessionId : user.getName(), e)
            return false
        }


    }

    public static void main(String[] args) {

        byte[] s = AccessController.class.getClassLoader().getResource("example.xls").getFile().getBytes()
        println s
    }

}
