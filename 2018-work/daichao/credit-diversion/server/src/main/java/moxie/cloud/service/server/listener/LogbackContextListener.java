//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package moxie.cloud.service.server.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.kv.model.GetValue;
import java.io.StringReader;
import java.util.Properties;
import java.util.function.Consumer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

public class LogbackContextListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    private static final Logger log = LoggerFactory.getLogger(LogbackContextListener.class);
    private boolean started = false;

    public LogbackContextListener() {
    }

    public void start() {
        if (!this.started) {
            Properties bootStrapProps = new Properties();

            String serviceName;
            try {
                bootStrapProps.load(this.getClass().getClassLoader().getResourceAsStream("bootstrap.properties"));
                serviceName = bootStrapProps.getProperty("moxie.cloud.service.name");
//                this.getContext().putProperty("BASE_LOG_PATH", "./" + serviceName);
            } catch (Throwable var8) {
                log.error("加载本地bootstrap.properties异常", var8);
                return;
            }

            String serviceTag = System.getProperty("service.tag");
            Properties props = new Properties();
            if (serviceTag != null && !serviceTag.equalsIgnoreCase("local")) {
                ConsulClient consulClient = new ConsulClient();
                String consulKey = "common/" + serviceTag + "/config";
                this.loadPropertyFromConsul(consulClient, consulKey, props);
                props.entrySet().forEach((prop) -> {
                    System.setProperty((String)prop.getKey(), (String)prop.getValue());
                });
                consulKey = "service/" + serviceName + "/" + serviceTag + "/config";
                this.loadPropertyFromConsul(consulClient, consulKey, props);
            } else {
                try {
                    props.load(this.getClass().getClassLoader().getResourceAsStream("application.properties"));
                } catch (Throwable var7) {
                    log.error("加载本地application.properties异常", var7);
                }
            }

            bootStrapProps.entrySet().forEach((prop) -> {
                this.getContext().putProperty((String)prop.getKey(), (String)prop.getValue());
            });
            props.entrySet().forEach((prop) -> {
                this.getContext().putProperty((String)prop.getKey(), (String)prop.getValue());
            });
            props.clear();
            this.started = true;
        }
    }

    private void loadPropertyFromConsul(ConsulClient consulClient, String consulKey, Properties props) {
        GetValue value;
        if (StringUtils.isBlank(System.getProperty("consul.token"))) {
            value = (GetValue)consulClient.getKVValue(consulKey).getValue();
        } else {
            value = (GetValue)consulClient.getKVValue(consulKey, System.getProperty("consul.token")).getValue();
        }

        if (value != null) {
            try {
                String encrptContent = value.getValue();
                if (StringUtils.isNotBlank(encrptContent)) {
                    props.load(new StringReader(new String(Base64Utils.decodeFromString(encrptContent), "UTF-8")));
                } else {
                    log.warn("consul key:" + consulKey + "配置内容为空");
                }
            } catch (Throwable var6) {
                log.error("加载consul key:" + consulKey + "配置异常", var6);
            }
        } else {
            log.error("无法从consul找到key:{}", consulKey);
        }

    }

    public void stop() {
    }

    public boolean isStarted() {
        return this.started;
    }

    public boolean isResetResistant() {
        return true;
    }

    public void onStart(LoggerContext context) {
    }

    public void onReset(LoggerContext context) {
    }

    public void onStop(LoggerContext context) {
    }

    public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
    }
}
