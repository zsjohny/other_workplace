package org.dream.utils.zookeeper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author Boyce 2016年6月21日 下午5:35:25
 */
public class ZkChildrenTest {
	public static ZooKeeper zk = null;
	static {
		try {
			zk = new ZooKeeper("192.168.0.88:2181", 60, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		reloadConf(null);
		while(true){}
	}

	public static void reloadConf(String path) {
		try {
			if (path == null) {
				List<String> confs = zk.getChildren("/sys_conf", notify);
				for (Iterator iterator = confs.iterator(); iterator.hasNext();) {
					String conf = (String) iterator.next();
					String data = new String(zk.getData("/sys_conf/"+conf, notify, new Stat()));
					// TODO
					System.out.println(data);
				}

			} else {
				String data = new String(zk.getData(path, notify, new Stat()));
				// TODO
				System.out.println(data);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static Watcher notify = new Watcher() {

		@Override
		public void process(WatchedEvent event) {
			System.out.println(event.getPath());
			if (event.getType() == EventType.NodeChildrenChanged) {
				reloadConf(null);
			} else if (event.getType() == EventType.NodeDataChanged) {
				reloadConf(event.getPath());
			}
		}
	};
}
