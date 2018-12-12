package org.dream.utils.zookeeper;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DistributedLock {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private String rootNode = ZookeeperServer.ROOT_NODE;

	public static String DEEFAULT_NODE = "distributed_thread_lock";

	private String key;

	private String nodeName;

	private Watcher watch;

	private static ZooKeeper zk;



	/**
	 * @param zkServer
	 *            Zookeeper服务器连接地址 ps: 192.168.1.88:8161
	 * @param key
	 *            加锁Key，字符串
	 */
	public DistributedLock(String key) {
		this.key = key;
		try {
			watch = new LockReleaseNotify(this);
			zk = ZookeeperServer.zk;
		} catch (Exception e) {// ZK服务器连接失败，使用本地内存锁
			// e.printStackTrace();
			logger.debug("使用本地线程锁，key:{}", key);
		}

	}
	/**
	 * 
	 *
	 * 描述:加锁,实际逻辑为在Zookeeper上创建临时节点 判断自己是否为最小节点，是，则正常运行，不是，等待
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午3:12:01
	 * @since v1.0.0
	 * @return void
	 */
	public void lock() {
			try {
				try {
					if (zk.exists(parentNode(), false) == null) {
						zk.create(parentNode(), "lock_root_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				String node = parentNode() + "/lock_";
				this.nodeName = zk.create(node, (System.currentTimeMillis() + "-task").getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL_SEQUENTIAL);
				// 尝试获得锁
				getLock();
			} catch (KeeperException | InterruptedException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 
	 *
	 * 描述:删除创建的临时节点
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午3:13:24
	 * @since v1.0.0
	 * @return void
	 */
	public void unlock() {
			try {
				zk.delete(this.nodeName, -1);
			} catch (InterruptedException | KeeperException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 
	 * 描述:尝试获得锁，未获得锁时，使用wait方法等待,阻塞
	 *
	 * @author jiupeng
	 * @created 2015年7月29日 下午5:29:19
	 * @since v1.0.0
	 * @return void
	 */
	private void getLock() {
		try {
			List<String> list = zk.getChildren(parentNode(), false);
			String miniNode = parentNode() + "/" + getMiniNodeName(list);
			if (!this.nodeName.equals(miniNode)) {
				Stat stat = zk.exists(miniNode, watch);
				if (stat != null) {
					synchronized (this) {
						this.wait();
						getLock();
					}
				} else {
					getLock();
				}
			}
		} catch (KeeperException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *
	 * 尝试获取锁，获取成功，返回true,否者返回false,成功或失败都会正常返回，不阻塞 <br/>
	 * 成功获取锁后，需要手动释放 <br/>
	 * 获取锁失败时，不需要手动释放
	 *
	 * @author jiupeng
	 * @created 2015年12月23日 上午11:22:58
	 * @since v1.0.0
	 * @return
	 * @return boolean
	 */
	public boolean tryLock() {
			try {
				if (zk.exists(parentNode(), false) == null) {
					zk.create(parentNode(), "lock_root_node".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
				}

				String node = parentNode() + "/lock_";
				this.nodeName = zk.create(node, (System.currentTimeMillis() + "-task").getBytes(), Ids.OPEN_ACL_UNSAFE,
						CreateMode.EPHEMERAL_SEQUENTIAL);
				List<String> list = zk.getChildren(parentNode(), false);
				String miniNode = parentNode() + "/" + getMiniNodeName(list);
				if (!this.nodeName.equals(miniNode)) {
					Stat stat = zk.exists(miniNode, watch);
					// 最小节点是否状态正常
					if (stat != null) {// 最小节点
						list = zk.getChildren(parentNode(), false);
						miniNode = parentNode() + "/" + getMiniNodeName(list);
						if (this.nodeName.equals(miniNode)) {
							return true;
						} else {
							zk.delete(this.nodeName, -1);
							return false;
						}
					} else {
						zk.delete(this.nodeName, -1);
						return false;
					}
				} else {
					return true;
				}
			} catch (KeeperException | InterruptedException e) {
				try {
					zk.delete(this.nodeName, -1);
				} catch (InterruptedException | KeeperException e1) {
					e1.printStackTrace();
				}
				return false;
			}

	}

	private String parentNode() {
		return rootNode + "/" + StringUtils.defaultIfBlank(key, DEEFAULT_NODE);
	}

	// 获取节点列表中最小节点
	private String getMiniNodeName(List<String> strs) {
		String args[] = new String[strs.size()];
		strs.toArray(args);
		Arrays.sort(args);
		return args[0];
	}

}
