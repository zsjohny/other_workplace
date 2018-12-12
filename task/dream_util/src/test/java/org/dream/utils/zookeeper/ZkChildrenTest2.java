package org.dream.utils.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

/**
 * @author Boyce 2016年6月21日 下午5:35:25
 */
public class ZkChildrenTest2 {
	public static void main(String[] args) {
		try {
			final ZooKeeper zk = new ZooKeeper("192.168.0.88:2181", 60, new Watcher() {

				@Override
				public void process(WatchedEvent event) {
				
				}
			});
//			zk.create("/sys_conf/cfg11", "hello".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			zk.setData("/sys_conf/cfg13", "hello6".getBytes(), -1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
