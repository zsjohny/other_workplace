package org.dream.utils.prop;

import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * 配置加载器
 * 
 * @author Boyce 2016年6月22日 上午9:35:17
 */
public class SpringProperties extends PropertyPlaceholderConfigurer {
	private Logger logger = LoggerFactory.getLogger(SpringProperties.class);
	private Properties props;
	CuratorFramework curatorClient;

	private static BeanFactory fac = null;

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		// TODO Auto-generated method stub
		super.setBeanFactory(beanFactory);
		fac = beanFactory;
	}

	public static Object getBean(String beanName) {
		return fac.getBean(beanName);
	}
	public static <T> T getBean(Class<T> requiredType){
		return fac.getBean(requiredType);
	}
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		this.props = props;
		curatorClient = (CuratorFramework) fac.getBean(CuratorFramework.class);

		if (props.getProperty("sys.conf.inzookeeper") != null) {
			reloadConf(null);
		}
		/**
		 * 系统在完成配置文件的加载之后要检测一下zookeeper中的根节点是否已经存在，如果不存在则添加此节点为根节点
		 * 这样为admin启动时将数据库中的配置检测或加载配置提供根节点基础， <strong></strong>
		 */
		if (props.getProperty("sys.conf.zk.root") != null) {
			try {
				curatorClient.getData().forPath(props.getProperty("sys.conf.zk.root"));
			} catch (Exception e) {
				try {
					curatorClient.create().forPath(props.getProperty("sys.conf.zk.root"));
				} catch (Exception e1) {
					logger.error("创建系统配置根节点失败",e1);;
				}
			}
		}
	}

	public  String getProperty(String key) {
		return props.getProperty(key);
	}

	private void reloadConf(String path) {
		logger.info("start loading/reloading systm conf from zookeeper");
		String zk_cfg_root = props.getProperty("sys.conf.zk.root");
		try {

			if (path == null) {// 遍历所有子节点来绑定属性
				List<String> confs = curatorClient.getChildren().usingWatcher(notify).forPath(zk_cfg_root);
				for (Iterator iterator = confs.iterator(); iterator.hasNext();) {
					String conf = (String) iterator.next();
					if (!props.containsKey(conf)) {
						String data = new String(
								curatorClient.getData().usingWatcher(notify).forPath(zk_cfg_root + "/" + conf));
						props.put(conf, data);
						logger.info("load conf {} value {} from zk!", conf, data);
					}
				}
			} else {// 访问变化的子节点来改变属性
				String data = new String(curatorClient.getData().usingWatcher(notify).forPath(path));
				String conf = path.substring(path.lastIndexOf("/"));
				props.put(conf, data);
				logger.info("load conf {} value {} from zk!", conf, data);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("end loading/reloading systm conf from zookeeper");

	}

	private void deleteConf(String path) {
		logger.info("node {} has been deleted! remove from spring props.", path);
		try {
			props.remove(path.substring(path.lastIndexOf("/") + 1));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	private Watcher notify = new Watcher() {

		@Override
		public void process(WatchedEvent event) {
			if (event.getType() == EventType.NodeChildrenChanged) {
				reloadConf(null);
			} else if (event.getType() == EventType.NodeDataChanged) {
				reloadConf(event.getPath());
			} else if (event.getType() == EventType.NodeDeleted) {
				deleteConf(event.getPath());
			}
		}
	};

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:/app_jms_p*.xml");
		while (true) {
		}
	}
}
