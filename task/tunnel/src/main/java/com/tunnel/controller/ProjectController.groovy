package com.tunnel.controller

import com.tunnel.domain.Project
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
@RequestMapping("project")
class ProjectController {
    @Autowired
    private BaseDomainServer baseDomainServer;
    private String REDIRECT = "redirect:"
    private Logger logger = LoggerFactory.getLogger(ProjectController.class)
    //添加用户
    @ResponseBody
    @RequestMapping("/modifyProject")
    public Integer modifyProject(HttpServletRequest request, Project project) {

        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return 1
        }
        User userOnline = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问添加项目页面", userOnline == null ? sessionId : userOnline.getName())

        try {

            if (!User.UserType.commontUser.equals(userOnline.getAuthorLevel())) {

                //检查项目名
                Project projectFind = baseDomainServer.findBaseDomainByBaseDomain(new Project("projectNumber": project.getProjectNumber()))

                if (project.getId() != null) {


                    if (projectFind != null && !projectFind.getId().equals(project.getId())) {
                        logger.info("项目{}已经存在,无需添加", project.getProjectNumber())
                        return 2


                    }

                    //存在继续修改
                    baseDomainServer.updateBaseDomainByBaseDomain(project)

                    logger.info("用户{},修改项目{}页面成功", userOnline == null ? sessionId : userOnline.getName(), project.getProjectNumber())
                    return 0


                } else {

                    if (projectFind != null) {
                        logger.info("项目{}已经存在,无需添加", project.getProjectNumber())
                        return 2
                    }
                    project.setCreateime(LocalDateTime.now())
                    baseDomainServer.saveBaseDomain(project)
                    logger.info("用户{},添加项目页面成功", userOnline == null ? sessionId : userOnline.getName())
                    return 0

                }
            }

        } catch (Exception e) {
            logger.warn("用户{},访问添加项目页面出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return 1

    }

    //查找用户
    @RequestMapping("/findProject")
    public String findProject(HttpServletRequest request, @RequestParam(value = "page", required = false) Integer page
                              ,
                              @RequestParam(value = "id", required = false) Integer id,
                              @RequestParam(value = "projectNumber", required = false) String projectNumber, ModelMap map) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return "index"
        }

        User userOnline = request.getSession().getAttribute(sessionId)
        map.put("authorLevel", userOnline.getAuthorLevel())
        logger.info("用户{},开始访问查找项目页面", userOnline == null ? sessionId : userOnline.getName())

        try {
            if (page <= 0 || page == null) {
                page = 1
            }

            Integer start = (page - 1) * BaseDomain.pageSize
            map.put("page", page)

            projectNumber = StringUtils.isEmpty(projectNumber) ? "" : projectNumber
            map.put("projectNumber", projectNumber)

            if (id != null) {
                Project project = baseDomainServer.findBaseDomainByBaseDomain(new Project("id": id))


                if (project == null) {
                    page = 1

                } else {

                    map.put("project", project)

                    return "project/edit"
                }

            }


            List<Project> list = baseDomainServer.findBaseDomainALlByPage(new Project("page": start, "projectNumber": projectNumber))
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
            saveMap.put("project", list)
            DownloadExcel.downloadExcelType.put(sessionId, saveMap)

            if (list == null || list.isEmpty()) {
                logger.info("没有项目数据!!")
                map.put("list", new ArrayList())

                return REDIRECT + "/login/againMenu.do?path=project/index"
            }
            map.put("list", list)

            Integer count = baseDomainServer.findBaseDomainCountByBaseDomain(new Project("projectNumber": projectNumber))

            //如果是一条的话直接展示编辑界面
            if (1.equals(count)) {
                map.put("project", list.get(0))
            }

            map.put("total", count)
            map.put("totalPage", (int) (Math.ceil(count / BaseDomain.pageSize)))
            logger.info("用户{},结束访问所有项目页面", userOnline == null ? sessionId : userOnline.getName())
            return "project/index"


        } catch (Exception e) {
            logger.warn("用户{},访问所有用户出错", userOnline == null ? sessionId : userOnline.getName(), e)
        }
        return "index"

    }

    //查找用户
    @RequestMapping("/removerProject")
    public String removerProject(HttpServletRequest request, Integer id, Integer page, String projectNumber) {
        String sessionId = CreatDatabase.getSessionId(request)

        if (StringUtils.isEmpty(sessionId)) {
            return REDIRECT + "/project/findProject.do?page=" + page
        }

        User user = request.getSession().getAttribute(sessionId)

        logger.info("用户{},开始访问删除项目页面", user == null ? sessionId : user.getName())

        try {

            if (!User.UserType.commontUser.equals(user.getAuthorLevel())) {

                Project projectFindById = baseDomainServer.findBaseDomainByBaseDomain(new Project("id": id))

                if (projectFindById == null) {
                    logger.warn("查找项目数据为空")
                    return REDIRECT + "/project/findProject.do?page=" + page

                }

                baseDomainServer.deleteBaseDomainById(new Project("id": id))
                logger.info("用户{},结束删除项目页面", user == null ? sessionId : user.getName())
                return REDIRECT + "/project/findProject.do?page=" + page + "&projectNumber=" + projectNumber
            }


        } catch (Exception e) {
            logger.warn("用户{},删除所有项目出错", user == null ? sessionId : user.getName(), e)
        }
        return REDIRECT + "/project/findProject.do?page=" + page
    }


}
