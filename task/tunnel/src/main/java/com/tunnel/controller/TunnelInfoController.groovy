package com.tunnel.controller

import com.tunnel.domain.Project
import com.tunnel.domain.TunnelInfo
import com.tunnel.domain.User
import com.tunnel.service.BaseDomainServer
import com.tunnel.util.BaseDomain
import com.tunnel.util.CreatDatabase
import com.tunnel.util.DownloadExcel
import com.tunnel.util.ExcelTypeTask
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody

import javax.servlet.http.HttpServletRequest
import java.time.LocalDateTime

/**
 * Created by Ness on 2016/10/14.
 */
@Controller
@RequestMapping("tunnelInfo")
class TunnelInfoController {
    @Autowired
    private BaseDomainServer baseDomainServer;
    private String REDIRECT = "redirect:"
    private Logger logger = LoggerFactory.getLogger(TunnelInfoController.class)
    //添加用户
    @ResponseBody
    @RequestMapping("/modifyTunnelInfo")
    public Integer modifyTunnelInfo(HttpServletRequest request, TunnelInfo tunnelInfo) {

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return 1
        }
        User userOnline = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问添加隧道信息页面", userOnline == null ? sessionId : userOnline.getName())

        try {

            if (!User.UserType.commontUser.equals(userOnline.getAuthorLevel())) {

                //检查隧道信息名
                TunnelInfo tunnelInfoFind = baseDomainServer.findBaseDomainByBaseDomain(new TunnelInfo("tunnelNumber": tunnelInfo.getTunnelNumber()))

                if (tunnelInfo.getId() != null) {


                    if (tunnelInfoFind != null && !tunnelInfoFind.getId().equals(tunnelInfo.getId())) {
                        logger.info("隧道信息{}已经存在,无需添加", tunnelInfo.getTunnelNumber())
                        return 2


                    }

                    //存在继续修改
                    baseDomainServer.updateBaseDomainByBaseDomain(tunnelInfo)

                    logger.info("用户{},修改隧道信息{}页面成功", userOnline == null ? sessionId : userOnline.getName(), tunnelInfo.getTunnelNumber())
                    return 0


                } else {

                    if (tunnelInfoFind != null) {
                        logger.info("隧道信息{}已经存在,无需添加", tunnelInfo.getTunnelNumber())
                        return 2
                    }
                    tunnelInfo.setCreateime(LocalDateTime.now())
                    baseDomainServer.saveBaseDomain(tunnelInfo)
                    logger.info("用户{},添加隧道信息页面成功", userOnline == null ? sessionId : userOnline.getName())
                    return 0

                }
            }

        } catch (Exception e) {
            logger.warn("用户{},访问添加隧道信息页面出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return 1

    }

    //查找隧道信息
    @RequestMapping("/findTunnelInfo")
    public String findTunnelInfo(HttpServletRequest request,
                                 @RequestParam(value = "page", required = false) Integer page
                                 ,
                                 @RequestParam(value = "id", required = false) Integer id,
                                 @RequestParam(value = "tunnelNumber", required = false) String tunnelNumber,
                                 @RequestParam(value = "pageType", required = false) String pageType, ModelMap map) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return "index"
        }

        User userOnline = request.getSession().getAttribute(sessionId)
        map.put("authorLevel", userOnline.getAuthorLevel())
        logger.info("用户{},开始访问查找隧道信息页面", userOnline == null ? sessionId : userOnline.getName())

        try {


            tunnelNumber = StringUtils.isEmpty(tunnelNumber) ? "" : tunnelNumber
            map.put("tunnelNumber", tunnelNumber)


            if (page <= 0 || page == null) {
                page = 1
            }

            Integer start = (page - 1) * BaseDomain.pageSize
            map.put("page", page)

            if (id != null && pageType != null) {
                TunnelInfo tunnelInfoFind = baseDomainServer.findBaseDomainByBaseDomain(new TunnelInfo("id": id))

                if (tunnelNumber != null) {


                    map.put("tunnelInfo", tunnelInfoFind)

                    List<Project> projects = baseDomainServer.findBaseDomainALlByPage(new Project())

                    if (projects == null || projects.isEmpty()) {
                        map.put("list", new ArrayList())

                    } else {
                        map.put("list", projects)
                    }
                    logger.info("用户{},结束访问部分隧道信息{}页面", userOnline == null ? sessionId : userOnline.getName(), tunnelInfoFind.getTunnelNumber())
                    return "tunnelInfo/" + (StringUtils.isEmpty(pageType) ? "edit" : pageType)
                }

            }



            List<TunnelInfo> list = baseDomainServer.findBaseDomainALlByPage(new TunnelInfo("page": start, "tunnelNumber": tunnelNumber))

            if (list == null || list.isEmpty()) {
                logger.info("没有隧道信息数据!!")
                map.put("list", new ArrayList())

                return REDIRECT + "/login/againMenu.do?path=tunnelInfo/index"
            }

            Integer count = baseDomainServer.findBaseDomainCountByBaseDomain(new TunnelInfo("tunnelNumber": tunnelNumber))

            //下载excel
            HashMap<String, List<BaseDomain>> saveMap
            if (DownloadExcel.downloadExcelType.get(sessionId) == null) {
                saveMap = new HashMap<>()
                ExcelTypeTask excelTypeTask = new ExcelTypeTask(sessionId)
                Timer timer = new Timer()
                timer.schedule(excelTypeTask,  1000 * 60*30)
            } else {
                saveMap = DownloadExcel.downloadExcelType.get(sessionId)

            }
            saveMap.put("tunnelInfo", list)
            DownloadExcel.downloadExcelType.put(sessionId, saveMap)

            //如果是一条的话直接展示编辑界面
            if (1.equals(count)) {
                map.put("tunnelInfo", list.get(0))
                List<Project> projects = baseDomainServer.findBaseDomainALlByPage(new Project())

                if (projects == null || projects.isEmpty()) {
                    map.put("list", new ArrayList())

                } else {
                    map.put("list", projects)
                }
            } else {
                map.put("list", list)
            }


            map.put("total", count)
            map.put("totalPage", (int) (Math.ceil(count / BaseDomain.pageSize)))
            logger.info("用户{},结束访问所有隧道信息页面", userOnline == null ? sessionId : userOnline.getName())
            return "tunnelInfo/index"


        } catch (Exception e) {
            logger.warn("用户{},访问所有用户出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return "index"

    }

    //删除隧道
    @RequestMapping("/removerTunnelInfo")
    public String removerTunnelInfo(HttpServletRequest request, Integer id, Integer page, String tunnelNumber) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return REDIRECT + "/tunnelInfo/findTunnelInfo.do?page=" + page
        }

        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问删除隧道信息页面", user == null ? sessionId : user.getName())

        try {

            if (!User.UserType.commontUser.equals(user.getAuthorLevel())) {

                TunnelInfo TunnelInfoFindById = baseDomainServer.findBaseDomainByBaseDomain(new TunnelInfo("id": id))

                if (TunnelInfoFindById == null) {
                    logger.warn("查找隧道信息数据为空")
                    return REDIRECT + "/tunnelInfo/findTunnelInfo.do?page=" + page

                }

                baseDomainServer.deleteBaseDomainById(new TunnelInfo("id": id))
                logger.info("用户{},结束删除隧道信息页面", user == null ? sessionId : user.getName())
                return REDIRECT + "/tunnelInfo/findTunnelInfo.do?page=" + page + "&tunnelNumber=" + tunnelNumber
            }


        } catch (Exception e) {
            logger.warn("用户{},删除所有隧道信息出错", user == null ? sessionId : user.getName(), e)
        }
        return REDIRECT + "/tunnelInfo/findTunnelInfo.do?page=" + page
    }


    class ResultsType implements Serializable {
        Boolean flag
        List<String> list

    }

    //查找所有项目
    @ResponseBody
    @RequestMapping("/findTunnelInfoByJson")
    public ResultsType findTunnelInfoByJson(HttpServletRequest request, String projectNumber) {
        String sessionId = CreatDatabase.getSessionId(request)


        ResultsType resultType = new ResultsType("flag": false)
        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(projectNumber)) {
            return resultType
        }

        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始根据ID查找相应项目下的隧道信息页面", user == null ? sessionId : user.getName())

        try {


            List<TunnelInfo> list = baseDomainServer.findBaseDomainALlByPage(new TunnelInfo("projectNumber": projectNumber))


            if (list == null || list.isEmpty()) {
                logger.warn("根据ID查找相应项目下的隧道信息数据为空")

                return resultType

            }


            List<String> numbers = new ArrayList<>()

            for (TunnelInfo info : list) {
                if (StringUtils.isEmpty(info.getTunnelNumber())) {
                    continue

                }
                numbers.add(info.getTunnelNumber())
            }

            resultType.setList(numbers)
            logger.info("用户{},结束根据ID查找相应项目下的隧道信息页面", user == null ? sessionId : user.getName())

            resultType.setFlag(true)

        } catch (Exception e) {
            logger.warn("用户{}查找根据ID查找相应项目下的隧道信息出错", user == null ? sessionId : user.getName(), e)

        }
        return resultType
    }

    enum pageFirstStatus {
        EXAMIN("examine", "首页调查按钮进来的"),
        QUERY("query", "首页查询按钮进来的"),
        CHOICE("choice", "首页选择按钮进来的");
        private String key;
        private String value;

        pageFirstStatus(String key, String value) {
            this.key = key
            this.value = value
        }

        public String getValue() {
            return key
        }

    }

    //查找所有项目
    @RequestMapping("/findAllProject")
    public String findAllProject(HttpServletRequest request, Boolean pageFirst,
                                 @RequestParam(value = "status", required = false) String status,
                                 @RequestParam(value = "params", required = false) String params,
                                 ModelMap map) {
        String sessionId = CreatDatabase.getSessionId(request)


        String url

        if (pageFirst) {
            //携带从添加页面返回过来的参数用户初始化页面，减少用户重复选择
            if (!StringUtils.isEmpty(params)) {
                String[] paraArr = params.split(":")

                map.put("tunnelProject", paraArr[0])
                map.put("tunnelNumber", paraArr[1])
                map.put("tunnelInfo", paraArr.length == 3 ? paraArr[2] : "")
            }

            if (pageFirstStatus.EXAMIN.getValue().equals(status)) {


                url = "/examine/index"
            } else if (pageFirstStatus.QUERY.getValue().equals(status)) {
                url = "/query/index"
            } else if (pageFirstStatus.CHOICE.getValue().equals(status)) {
                url = "/choice/index"
            }


        } else {
            url = "/tunnelInfo/add"
        }



        if (StringUtils.isEmpty(sessionId)) {

            return url
        }

        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始查找项目所有信息页面", user == null ? sessionId : user.getName())

        try {

            if (!User.UserType.commontUser.equals(user.getAuthorLevel())) {


                List<Project> list = baseDomainServer.findBaseDomainALlByPage(new Project())


                if (list == null || list.isEmpty()) {
                    logger.warn("查找项目所有信息数据为空")
                    return url

                }
                map.put("list", list)
                logger.info("用户{},结束查找项目所有信息页面", user == null ? sessionId : user.getName())

                return url
            }


        } catch (Exception e) {
            logger.warn("用户{}查找项目所有信息出错", user == null ? sessionId : user.getName(), e)

        }
        return url
    }

}
