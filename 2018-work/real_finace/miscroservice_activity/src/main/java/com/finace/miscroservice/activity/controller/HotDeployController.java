package com.finace.miscroservice.activity.controller;

import com.alipay.jarslink.api.Action;
import com.alipay.jarslink.api.Module;
import com.alipay.jarslink.api.ModuleConfig;
import com.alipay.jarslink.api.impl.ModuleLoaderImpl;
import com.alipay.jarslink.api.impl.ModuleManagerImpl;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.Iptools;
import com.finace.miscroservice.commons.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * 热部署控制器
 */
@RestController
@RequestMapping("activity")
public class HotDeployController {

    @Autowired
    private ModuleLoaderImpl loader;

    @Autowired
    private ModuleManagerImpl manager;

    @Value("${deployDownloadUrl}")
    private String deployDownloadUrl;


    private Log log = Log.getInstance(HotDeployController.class);


    private final String DEFAULT_VERSION = "1.0.0";
    private final String FILE_SUFFIX_NAME = ".jar";
    private final String SAVE_FILE_NAME = System.getProperty("java.io.tmpdir") + File.separator + "host_deploy";

    {
        File file = new File(SAVE_FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 加载服务
     *
     * @param request 请求
     * @param name    服务的名称
     * @return
     */
    @PostMapping("load")
    public Response loadManager(HttpServletRequest request, String name) {
        if (name == null || name.isEmpty()) {
            log.warn("IP={}所传部署的文件名为空", Iptools.gainRealIp(request));
            return Response.fail();
        }
        try {

            if (!registerModule(name)) {
                return Response.fail();
            }

            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(SAVE_FILE_NAME), true), "utf-8"))) {
                writer.write(name);
                writer.newLine();
                writer.flush();
            }


        } catch (Exception e) {
            log.error("IP={}部署服务名称={}出现异常", Iptools.gainRealIp(request), name, e);
            return Response.fail();
        }

        log.info("文件={}部署成功", name);
        return Response.success();
    }


    /**
     * 查询具体活动的操作
     *
     * @param request 请求
     * @param name    活动的名称
     * @param param   活动的参数
     * @return
     * @throws IOException
     */
    @PostMapping("query")
    public Response query(HttpServletRequest request, String name, @RequestParam(value = "param", required = false) String param) {

        if (name == null || name.isEmpty()) {
            log.warn("IP={}所传部署的文件名为空", Iptools.gainRealIp(request));
            return Response.fail();
        }
        try {


            Module module = manager.find(name, DEFAULT_VERSION);

            if (module == null) {
                log.warn("寻找不到服务名称={}", name);
                return Response.fail();

            }

            ModuleConfig moduleConfig = module.getModuleConfig();

            if (moduleConfig instanceof RModuleConfig) {

                RModuleConfig rModuleConfig = (RModuleConfig) moduleConfig;

                if (!rModuleConfig.getEndTime().equals(NEVER_INVALID_TIME) && System.currentTimeMillis() > rModuleConfig.getEndTime()) {
                    log.warn("服务={} 已过期", rModuleConfig.getName());
                    return Response.fail();
                }

            }
            Map<String, Action> actions = module.getActions();

            Object result = null;
            for (Action action : actions.values()) {
                result = action.execute(param);
            }

            log.info("服务={}执行参数={}执行成功", name, param);
            return Response.success(result);

        } catch (Exception e) {

            log.error("服务={}执行参数={}执行异常", name, param, e);
            return Response.fail();

        }


    }

    @PostConstruct
    public void restart() throws FileNotFoundException {

        new BufferedReader(new InputStreamReader(new FileInputStream(new File(SAVE_FILE_NAME)))).lines().forEach((x) -> {

            try {
                registerModule(x);

            } catch (MalformedURLException e) {
                log.warn("服务名{}未找到,加载失败", x);
            }
        });


    }

    private Map<String, Boolean> registerCache = new ConcurrentHashMap<>();


    /**
     * 注册服务
     *
     * @param moduleName 服务名称
     * @throws MalformedURLException
     */
    private Boolean registerModule(String moduleName) throws MalformedURLException {

        Boolean registerFlag = Boolean.FALSE;
        if (!pattern.matcher(moduleName).matches()) {
            log.warn("服务={} 不符合名称规范[name-startTime_endTime]", moduleName);
            return registerFlag;
        }


        String[] nameArr = moduleName.split(MODULE_NAME_SPLIT);

        if (registerCache.containsKey(nameArr[0])) {
            log.info("服务={}已经注册过", nameArr[0]);
            return registerFlag;
        }
        String[] timeArr = nameArr[1].split(TIME_LINK_SPLIT);
        RModuleConfig moduleConfig = new RModuleConfig();
        Long startTime = Long.valueOf(timeArr[0]);
        Long endTime = Long.valueOf(timeArr[1]);

        if (!startTime.equals(NEVER_INVALID_TIME) && startTime > endTime) {
            log.warn("服务={}的开始时间={}和结束时间={}填写不正确", nameArr[0], startTime, endTime);
            return registerFlag;
        }
        if (!endTime.equals(NEVER_INVALID_TIME) && System.currentTimeMillis() > endTime) {
            log.warn("服务={} 已过期", nameArr[0]);
            return registerFlag;
        }

        moduleConfig.setStartTime(startTime);
        moduleConfig.setEndTime(endTime);
        moduleConfig.setName(nameArr[0]);
        moduleConfig.setEnabled(Boolean.TRUE);
        moduleConfig.setVersion(DEFAULT_VERSION);
        moduleConfig.addModuleUrl(new URL(deployDownloadUrl + moduleName + FILE_SUFFIX_NAME));
        manager.register(loader.load(moduleConfig));

        log.info("服务名={}注册成功", nameArr[0]);
        registerCache.put(nameArr[0], Boolean.TRUE);
        registerFlag = Boolean.TRUE;
        return registerFlag;

    }


    private final String MODULE_NAME_SPLIT = "-";
    private final String TIME_LINK_SPLIT = "_";
    private final Long NEVER_INVALID_TIME = 0L;

    private final Pattern pattern = Pattern.compile("\\w+" + MODULE_NAME_SPLIT + "\\d+" + TIME_LINK_SPLIT + "\\d+");


    class RModuleConfig extends ModuleConfig {

        private Long startTime;
        private Long endTime;

        public Long getStartTime() {
            return startTime;
        }

        public void setStartTime(Long startTime) {
            this.startTime = startTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public void setEndTime(Long endTime) {
            this.endTime = endTime;
        }
    }

}
