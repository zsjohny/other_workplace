package com.tunnel.controller

import com.tunnel.domain.*
import com.tunnel.service.BaseDomainServer
import com.tunnel.util.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.multipart.MultipartFile

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime

/**
 * 隧道信息总控制层
 * Created by Ness on 2016/10/18.
 */
@Controller
@RequestMapping("tunnelInfoTotal")
class TunnelInfoTotalController {
    private String REDIRECT = "redirect:"

    private Logger logger = LoggerFactory.getLogger(TunnelInfoTotalController.class)

    @Autowired
    private BaseDomainServer baseDomainServer;
    //添加隧道信息
    @ResponseBody
    @RequestMapping("/modifyTunnelInfoTotal")
    public Boolean modifyTunnelInfoTotal(HttpServletRequest request, ClassType classType,
                                         TunnelImportPort tunnelImportPort, TunnelShallowCover tunnelShallowCover, AssistTunnel assistTunnel
                                         , TunnelGrabageExamine tunnelGrabageExamine, TunnelHeadRiskExamine tunnelHeadRiskExamine, OverBreakRiskExmaine overBreakRiskExmaine, SurgeMudRiskExamine surgeMudRiskExamine,
                                         ShapeRiskExamine shapeRiskExamine, RockOutburstRiskExamine rockOutburstRiskExamine, GasRiskExamine gasRiskExamine, FireRiskExmaine fireRiskExmaine,
                                         TrafficAccidentRiskExamine trafficAccidentRiskExamine, EnvirRiskExamine envirRiskExamine,
                                         OtherRiskExamine otherRiskExamine, Figure figure) {

        //判断是否是新增还是修改
        Boolean isAddFlag = true
        BaseDomain baseDomain

        switch (classType.getClassName()) {
            case ClassType.ClassTypeMapping.tunnelImportPort.getKey():

                if (tunnelImportPort.getId() != null) {
                    isAddFlag = false

                }

                tunnelImportPort.setCreateime(LocalDateTime.now())

                baseDomain = tunnelImportPort
                break;

            case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():

                if (tunnelShallowCover.getId() != null) {
                    isAddFlag = false

                }

                tunnelShallowCover.setCreateime(LocalDateTime.now())

                baseDomain = tunnelShallowCover
                break;
            case ClassType.ClassTypeMapping.assistTunnel.getKey():

                if (assistTunnel.getId() != null) {
                    isAddFlag = false

                }

                assistTunnel.setCreateime(LocalDateTime.now())

                baseDomain = assistTunnel
                break;
            case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():

                if (tunnelGrabageExamine.getId() != null) {
                    isAddFlag = false

                }

                tunnelGrabageExamine.setCreateime(LocalDateTime.now())

                baseDomain = tunnelGrabageExamine
                break;

            case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():

                if (tunnelHeadRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                tunnelHeadRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = tunnelHeadRiskExamine
                break;

            case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():

                if (overBreakRiskExmaine.getId() != null) {
                    isAddFlag = false

                }

                overBreakRiskExmaine.setCreateime(LocalDateTime.now())

                baseDomain = overBreakRiskExmaine
                break;
            case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():

                if (surgeMudRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                surgeMudRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = surgeMudRiskExamine
                break;
            case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():

                if (shapeRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                shapeRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = shapeRiskExamine
                break;
            case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():

                if (rockOutburstRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                rockOutburstRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = rockOutburstRiskExamine
                break;

            case ClassType.ClassTypeMapping.gasRiskExamine.getKey():

                if (gasRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                gasRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = gasRiskExamine
                break;

            case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():

                if (fireRiskExmaine.getId() != null) {
                    isAddFlag = false

                }

                fireRiskExmaine.setCreateime(LocalDateTime.now())

                baseDomain = fireRiskExmaine
                break;

            case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():

                if (trafficAccidentRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                trafficAccidentRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = trafficAccidentRiskExamine
                break;
            case ClassType.ClassTypeMapping.envirRiskExamine.getKey():

                if (envirRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                envirRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = envirRiskExamine
                break;

            case ClassType.ClassTypeMapping.otherRiskExamine.getKey():

                if (otherRiskExamine.getId() != null) {
                    isAddFlag = false

                }

                otherRiskExamine.setCreateime(LocalDateTime.now())

                baseDomain = otherRiskExamine
                break;
            case ClassType.ClassTypeMapping.figure.getKey():

                if (figure.getId() != null) {
                    isAddFlag = false

                }

                figure.setCreateime(LocalDateTime.now())

                baseDomain = figure


        }

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return false
        }
        User userOnline = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问添加隧道编号{},的表{}信息填写页面", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())

        try {


            if (!User.UserType.commontUser.equals(userOnline.getAuthorLevel())) {

                TunnelExamineRelationQuery relationQuery = new TunnelExamineRelationQuery("tunnelNumber": classType.getTunnelNumber(), "tableName": classType.getClassName(), "createTime": LocalDateTime.now())

                //先查询调查跟表格的关系是否存在
                if (baseDomainServer.findBaseDomainByBaseDomain(relationQuery) == null) {
                    //添加调查和表格的关系
                    baseDomainServer.saveTunnelExmaineRelationQuery(relationQuery)
                    logger.info("隧道编号{},已经成功存储隧道信息表{}", classType.getTunnelNumber(), classType.getClassName())
                }


                if (!isAddFlag) {

                    //存在继续修改
                    baseDomainServer.updateBaseDomainByBaseDomain(baseDomain)

                    logger.info("用户{},修改隧道编号{},的表{}信息页面成功", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())
                    return true


                } else {

                    baseDomainServer.saveBaseDomain(baseDomain)

                    logger.info("用户{},添加隧道编号{},的表{}信息页面成功", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())
                    return true

                }
            }

        } catch (Exception e) {
            logger.warn("用户{},访问修改或者添加隧道的编号{},的表{}信息页面出错", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName(), e)
        }
        return false

    }

    //查找隧道填写的相应表信息
    @RequestMapping("/findTunnelInfoTotal")
    public String findTunnelInfoTotal(HttpServletRequest request,
                                      ClassType classType, @RequestParam(value = "page", required = false) Integer page
                                      ,
                                      @RequestParam(value = "id", required = false) Integer id,
                                      @RequestParam(value = "params", required = false) String params,
                                      @RequestParam(value = "code", required = false) Boolean code,
                                      @RequestParam(value = "pageName", required = false) String pageName,
                                      @RequestParam(value = "pageType", required = false) String pageType,
                                      ModelMap map) {


        map.put("tunnelNumber", classType.getTunnelNumber())
        map.put("pageName", StringUtils.isEmpty(pageName) ? "" : pageName)


        if (page <= 0 || page == null) {
            page = 1
        }

        if (!StringUtils.isEmpty(params)) {
            if (code != null && code) {
                params = URLDecoder.decode(params, "utf-8")
            }
            map.put("params", params)
        }

        map.put("page", page)

        Integer start = (page - 1) * BaseDomain.pageSize

        //因为附图有加载首页加载图片的功能所以这里直接展示出来最好
        Boolean isFigure = false

        BaseDomain baseDomain

        switch (classType.getClassName()) {
            case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
                baseDomain = new TunnelImportPort("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)
                break;
            case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():

                baseDomain = new TunnelShallowCover("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)
                break;
            case ClassType.ClassTypeMapping.assistTunnel.getKey():

                baseDomain = new AssistTunnel("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)
                //用来展示图片
                isFigure = true

                break;
            case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():

                baseDomain = new TunnelGrabageExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)
                break;

            case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
                baseDomain = new TunnelHeadRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;

            case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():

                baseDomain = new OverBreakRiskExmaine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;
            case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():

                baseDomain = new SurgeMudRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;
            case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():


                baseDomain = new ShapeRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;
            case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
                baseDomain = new RockOutburstRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;
            case ClassType.ClassTypeMapping.gasRiskExamine.getKey():

                baseDomain = new GasRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;

            case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():

                baseDomain = new FireRiskExmaine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;

            case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():

                baseDomain = new TrafficAccidentRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)


                break;
            case ClassType.ClassTypeMapping.envirRiskExamine.getKey():

                baseDomain = new EnvirRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;

            case ClassType.ClassTypeMapping.otherRiskExamine.getKey():

                baseDomain = new OtherRiskExamine("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)

                break;
            case ClassType.ClassTypeMapping.figure.getKey():
                baseDomain = new Figure("tunnelNumber": classType.getTunnelNumber(), "id": id, "page": start)
                //用来展示图片
                isFigure = true

        }


        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return "tunnelInfoTotal/" + classType.getClassName() + "Index"
        }

        User userOnline = request.getSession().getAttribute(sessionId)
        map.put("authorLevel", userOnline.getAuthorLevel())
        logger.info("用户{},开始查找隧道编号{},的表{}信息页面", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())

        try {


            if (id != null) {
                baseDomain = baseDomainServer.findBaseDomainByBaseDomain(baseDomain)

                map.put("baseDomain", baseDomain)


                logger.info("用户{},结束查找隧道编号{},的表{}信息页面", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())
                return "tunnelInfoTotal/" + classType.getClassName() + (StringUtils.isEmpty(pageType) ? "Edit" : pageType)

            }

            List<BaseDomain> lists = baseDomainServer.findBaseDomainALlByPage(baseDomain)

            if (lists == null || lists.isEmpty()) {
                logger.info("没有隧道编号{},表{}信息数据!!", classType.getTunnelNumber(), classType.getClassName())
                map.put("list", new ArrayList())

                return "tunnelInfoTotal/" + classType.getClassName() + "Index"
            }

            //下载excel
            HashMap<String, List<BaseDomain>> saveMap
            if (DownloadExcel.downloadExcelType.get(sessionId) == null) {
                saveMap = new HashMap<>()
                ExcelTypeTask excelTypeTask = new ExcelTypeTask(sessionId)
                Timer timer = new Timer()
                timer.schedule(excelTypeTask, 1000 * 60 * 30)
            } else {
                saveMap = DownloadExcel.downloadExcelType.get(sessionId)

            }



            saveMap.put(classType.getClassName(), lists)
            DownloadExcel.downloadExcelType.put(sessionId, saveMap)

            //如果是附图则用来展示图片，进行特殊处理
            if (isFigure) {
                if (classType.getClassName().equals(ClassType.ClassTypeMapping.figure.getKey())) {


                    List<Figure> figureList = new ArrayList<>()
                    for (BaseDomain base : lists) {
                        Figure dealFigure = (Figure) base
                        if (dealFigure == null) {
                            continue
                        }
                        //图片路径为空的时候进行添加到list
                        if (StringUtils.isEmpty(dealFigure.getPhotosPicture())) {
                            figureList.add(dealFigure)
                            continue
                        }

                        String[] paths = dealFigure.getPhotosPicture().split(":")
                        StringBuilder sb = new StringBuilder()
                        for (String str : paths) {
                            if (str == null || str.trim().length() == 0) {
                                continue
                            }
                            sb.append("<img class='img-responsive center-block' src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=figure&name=" + str + "'/>")
                        }

                        dealFigure.setPhotosPicture(sb.toString())
                        figureList.add(dealFigure)

                    }
                    lists = figureList


                } else {


                    List<AssistTunnel> assistTunnels = new ArrayList<>()
                    for (BaseDomain base : lists) {
                        AssistTunnel dealAssistTunnel = (AssistTunnel) base
                        if (dealAssistTunnel == null) {
                            continue
                        }
                        //图片路径为空的时候进行添加到list
                        if (StringUtils.isEmpty(dealAssistTunnel.getAcccessTunnelAndAirshaftRelationWithMainTunnel())) {
                            assistTunnels.add(dealAssistTunnel)
                            continue
                        }

                        String[] paths = dealAssistTunnel.getAcccessTunnelAndAirshaftRelationWithMainTunnel().split(":")
                        StringBuilder sb = new StringBuilder()
                        for (String str : paths) {
                            if (str == null || str.trim().length() == 0) {
                                continue
                            }
                            sb.append("<img class='img-responsive center-block' src='/tunnel/tunnelInfoTotal/disPlayPhotos.do?className=assistTunnel&name=" + str + "'/>")
                        }

                        dealAssistTunnel.setAcccessTunnelAndAirshaftRelationWithMainTunnel(sb.toString())
                        assistTunnels.add(dealAssistTunnel)

                    }
                    lists = assistTunnels


                }
            }



            map.put("list", lists)
            Integer count = baseDomainServer.findBaseDomainCountByBaseDomain(baseDomain)
            //如果是一条的话直接展示编辑界面
            if (1.equals(count)) {
                //图片再次特殊处理
                if (isFigure) {
                    baseDomain = baseDomainServer.findBaseDomainByBaseDomain(lists.get(0))
                    map.put("baseDomain", baseDomain)
                } else {
                    map.put("baseDomain", lists.get(0))
                }


            }


            map.put("total", count)
            map.put("totalPage", (int) (Math.ceil(count / BaseDomain.pageSize)))
            logger.info("用户{},结束查找隧道编号{},的表{}总信息页面", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName())

            return "tunnelInfoTotal/" + classType.getClassName() + "Index"

        } catch (Exception e) {
            logger.info("用户{},查找隧道编号{},的表{}信息页面失败", userOnline == null ? sessionId : userOnline.getName(), classType.getTunnelNumber(), classType.getClassName(), e)
        }
        return "tunnelInfoTotal/" + classType.getClassName() + "Index"
    }


    class ResultsType implements Serializable {
        Boolean flag
        List<String> list

    }

    //根据隧道编号查询相应的所有隧道表信息
    @ResponseBody
    @RequestMapping("/findTunnelInfoTotalByJson")
    public ResultsType findTunnelInfoByJson(HttpServletRequest request, TunnelExamineRelationQuery tunnelExamineRelationQuery) {
        String sessionId = CreatDatabase.getSessionId(request)


        ResultsType resultType = new ResultsType("flag": false)
        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(tunnelExamineRelationQuery.getTunnelNumber())) {
            return resultType
        }


        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始根据隧道编号{}查询相应的所有隧道表信息{}页面", user == null ? sessionId : user.getName(), tunnelExamineRelationQuery.getTunnelNumber(), tunnelExamineRelationQuery.getTableName())

        try {


            List<TunnelExamineRelationQuery> list = baseDomainServer.findBaseDomainALlByPage(tunnelExamineRelationQuery)


            if (list == null || list.isEmpty()) {
                logger.info("用户{},根据隧道编号{}查询相应的所有隧道表信息{}页面,数据为空", user == null ? sessionId : user.getName(), tunnelExamineRelationQuery.getTunnelNumber(), tunnelExamineRelationQuery.getTableName())
                resultType.setList(new ArrayList<String>())
                return resultType

            }


            List<String> numbers = new ArrayList<>()

            for (TunnelExamineRelationQuery info : list) {
                if (StringUtils.isEmpty(info.getTableName())) {
                    continue

                }
                numbers.add(info.getTableName())
            }

            resultType.setList(numbers)
            logger.info("用户{},结束根据隧道编号{}查询相应的所有隧道表信息{}页面", user == null ? sessionId : user.getName(), tunnelExamineRelationQuery.getTunnelNumber(), tunnelExamineRelationQuery.getTableName())

            resultType.setFlag(true)

        } catch (Exception e) {
            logger.warn("用户{},根据隧道编号{}查询相应的所有隧道表信息{}页面出错", user == null ? sessionId : user.getName(), tunnelExamineRelationQuery.getTunnelNumber(), tunnelExamineRelationQuery.getTableName(), e)

        }
        return resultType
    }

    //删除隧道表格信息
    @RequestMapping("/removerTunnelInfoTotal")
    public String removerTunnelInfoTotal(HttpServletRequest request, Integer id, Integer page, ClassType classType, String params,
                                         @RequestParam(value = "pageName", required = false) String pageName) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return "index"
        }


        BaseDomain baseDomain


        switch (classType.getClassName()) {
            case ClassType.ClassTypeMapping.tunnelImportPort.getKey():
                baseDomain = new TunnelImportPort("id": id)
                break;
            case ClassType.ClassTypeMapping.tunnelShallowCover.getKey():

                baseDomain = new TunnelShallowCover("id": id)
                break;
            case ClassType.ClassTypeMapping.assistTunnel.getKey():

                baseDomain = new AssistTunnel("id": id)
                break;
            case ClassType.ClassTypeMapping.tunnelGrabageExamine.getKey():

                baseDomain = new TunnelGrabageExamine("id": id)
                break;

            case ClassType.ClassTypeMapping.tunnelHeadRiskExamine.getKey():
                baseDomain = new TunnelHeadRiskExamine("id": id)

                break;

            case ClassType.ClassTypeMapping.overBreakRiskExmaine.getKey():

                baseDomain = new OverBreakRiskExmaine("id": id)

                break;
            case ClassType.ClassTypeMapping.surgeMudRiskExamine.getKey():

                baseDomain = new SurgeMudRiskExamine("id": id)

                break;
            case ClassType.ClassTypeMapping.shapeRiskExamine.getKey():


                baseDomain = new ShapeRiskExamine("id": id)

                break;
            case ClassType.ClassTypeMapping.rockOutburstRiskExamine.getKey():
                baseDomain = new RockOutburstRiskExamine("id": id)

                break;
            case ClassType.ClassTypeMapping.gasRiskExamine.getKey():

                baseDomain = new GasRiskExamine("id": id)

                break;

            case ClassType.ClassTypeMapping.fireRiskExmaine.getKey():

                baseDomain = new FireRiskExmaine("id": id)

                break;

            case ClassType.ClassTypeMapping.trafficAccidentRiskExamine.getKey():

                baseDomain = new TrafficAccidentRiskExamine("id": id)


                break;
            case ClassType.ClassTypeMapping.envirRiskExamine.getKey():

                baseDomain = new EnvirRiskExamine("id": id)

                break;

            case ClassType.ClassTypeMapping.otherRiskExamine.getKey():

                baseDomain = new OtherRiskExamine("id": id)

                break;
            case ClassType.ClassTypeMapping.figure.getKey():
                baseDomain = new Figure("id": id)


        }



        User user = request.getSession().getAttribute(sessionId)


        try {
            logger.info("用户{},开始删除根据隧道编号{}查询相应的所有隧道表信息{}页面", user == null ? sessionId : user.getName(), classType.getTunnelNumber(), classType.getClassName())

            if (!User.UserType.commontUser.equals(user.getAuthorLevel())) {

                baseDomain = baseDomainServer.findBaseDomainByBaseDomain(baseDomain)

                if (baseDomain == null) {
                    logger.info("用户{},据隧道编号{}查询相应的所有隧道表信息{}数据为空", user == null ? sessionId : user.getName(), classType.getTunnelNumber(), classType.getClassName())

                } else {

                    //如果是最后一条则删除对应关系
                    Integer count = baseDomainServer.findBaseDomainCountByBaseDomain(baseDomain)
                    if (count != null && count == 1) {
                        TunnelExamineRelationQuery relationQuery = new TunnelExamineRelationQuery("tunnelNumber": classType.getTunnelNumber(), "tableName": classType.getClassName())
                        //先查询调查跟表格的关系是否存在
                        relationQuery = baseDomainServer.findBaseDomainByBaseDomain(relationQuery)
                        if (relationQuery != null) {
                            baseDomainServer.deleteBaseDomainById(relationQuery)
                            logger.info("用户{},隧道编号{},已经删除存储隧道信息表{}", user == null ? sessionId : user.getName(), classType.getTunnelNumber(), classType.getClassName())
                        }
                    }




                    baseDomainServer.deleteBaseDomainById(baseDomain)
                    logger.info("用户{},删除据隧道编号{}查询相应的所有隧道表信息{}数据成功", user == null ? sessionId : user.getName(), classType.getTunnelNumber(), classType.getClassName())
                }

            }


        } catch (Exception e) {
            logger.warn("用户{},删除据隧道编号{}查询相应的所有隧道表信息{}数据失败", user == null ? sessionId : user.getName(), classType.getTunnelNumber(), classType.getClassName(), e)
        }
        params = URLEncoder.encode(params, "utf-8")
        return REDIRECT + "/tunnelInfoTotal/findTunnelInfoTotal.do?page=" + page + "&className=" + classType.getClassName() + "&tunnelNumber=" + classType.getTunnelNumber() + "&pageName=" + (StringUtils.isEmpty(pageName) ? "" : pageName) + "&code=true&params=" + params
    }

    //保存目录
    private
    static String savepath = TunnelInfoTotalController.class.getClassLoader().getResource("").getPath().substring(1) + "temp/"

    //上传照片
    @ResponseBody
    @RequestMapping("/uploadPhotos")
    public String uploadPhotos(HttpServletRequest request,
                               @RequestParam("files") MultipartFile[] files, ClassType classType) {
        String sessionId = CreatDatabase.getSessionId(request)
        String responsePath = ""
        if (StringUtils.isEmpty(sessionId) || files == null || files.length == 0 || classType == null || StringUtils.isEmpty(classType.getClassName())) {
            return responsePath


        }


        User user = request.getSession().getAttribute(sessionId)


        try {
            logger.info("用户{},开始上传拍摄照片", user == null ? sessionId : user.getName())

            File file = new File(savepath + classType.getClassName())
            if (!file.exists()) {
                file.mkdirs()
            }

            StringBuilder sb = new StringBuilder()

            //开始批量存储数据
            for (MultipartFile uploadFile : files) {
                if (uploadFile == null || uploadFile.isEmpty()) {
                    continue
                }
                String fileName = System.nanoTime() + ".png"
                file = new File(savepath + classType.getClassName(), fileName)
                uploadFile.transferTo(file)
                sb.append(fileName + ":")
            }
            if (sb.length() != 0) {
                logger.info("用户{},结束上传拍摄照片", user == null ? sessionId : user.getName())
                responsePath = sb.toString()
            }

        } catch (Exception e) {

            logger.warn("用户{},上传拍摄照片失败", user == null ? sessionId : user.getName(), e)
        }

        return responsePath

    }

    //删除图片和存储数据
    @ResponseBody
    @RequestMapping("delPhoto")
    public Boolean delPhoto(HttpServletRequest request, String name, Long id, ClassType classType) {

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId) || StringUtils.isEmpty(name) || classType == null || StringUtils.isEmpty(classType.getClassName())) {
            return false


        }

        User user = request.getSession().getAttribute(sessionId)


        try {
            logger.info("用户{},开始删除照片{}", user == null ? sessionId : user.getName(), name)
            if (!User.UserType.commontUser.equals(user.getAuthorLevel())) {

                File file = new File(savepath + classType.getClassName(), name)
                if (!file.exists()) {
                    logger.warn("{}文件不存在!!", name)
                    return false
                }
                String path = ""

                if (classType.getClassName().equals(ClassType.ClassTypeMapping.assistTunnel.getKey())) {
                    AssistTunnel assistTunnel = baseDomainServer.findBaseDomainByBaseDomain(new AssistTunnel("id": id))

                    if (assistTunnel == null) {
                        logger.warn("Id{},数据不存在!!", id)
                        return false
                    }

                    path = assistTunnel.getAcccessTunnelAndAirshaftRelationWithMainTunnel()


                } else {
                    Figure figure = baseDomainServer.findBaseDomainByBaseDomain(new Figure("id": id))
                    if (figure == null) {
                        logger.warn("Id{},数据不存在!!", id)
                        return false
                    }

                    path = figure.getPhotosPicture()
                }






                StringBuilder sb = new StringBuilder()
                if (!StringUtils.isEmpty(path)) {
                    String[] strArr = path.split(":")

                    for (String st : strArr) {
                        if (StringUtils.isEmpty(st.trim())) {
                            continue
                        }
                        if (!st.contains(name)) {
                            sb.append(st + ":")
                        }


                    }

                }
                if (classType.getClassName().equals(ClassType.ClassTypeMapping.assistTunnel.getKey())) {

                    baseDomainServer.updateBaseDomainByBaseDomain(new AssistTunnel("acccessTunnelAndAirshaftRelationWithMainTunnel": sb.toString(), "id": id, "createime": LocalDateTime.now()))

                } else {

                    baseDomainServer.updateBaseDomainByBaseDomain(new Figure("photosPicture": sb.toString(), "id": id, "createime": LocalDateTime.now()))
                }



                file.delete()

                logger.info("用户{},结束删除照片{}", user == null ? sessionId : user.getName(), name)
                return true
            }


        } catch (Exception e) {

            logger.info("用户{},删除照片{}失败", user == null ? sessionId : user.getName(), name, e)
        }


        return false
    }

    //预览照片
    @RequestMapping("/disPlayPhotos")
    public void displayPhotos(HttpServletRequest request, HttpServletResponse response, String name, ClassType classType) {

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId) || classType == null || StringUtils.isEmpty(classType.getClassName())) {
            return


        }

        User user = request.getSession().getAttribute(sessionId)

        OutputStream stream = response.getOutputStream()
        try {
            logger.info("用户{},开始预览拍摄照片", user == null ? sessionId : user.getName())

            File file = new File(savepath + classType.getClassName(), name)
            if (!file.exists()) {
                logger.warn("还没创建文件夹")
                return
            }


            byte[] bs = Files.readAllBytes(Paths.get(file.getPath()))

            stream.write(bs)


            logger.info("用户{},结束预览拍摄照片", user == null ? sessionId : user.getName())

        } catch (Exception e) {

            logger.warn("用户{},预览拍摄照片失败", user == null ? sessionId : user.getName(), e)
        } finally {
            stream.close()
        }

        return
    }


}


