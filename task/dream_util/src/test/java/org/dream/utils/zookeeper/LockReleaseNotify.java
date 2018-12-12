package org.dream.utils.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;

public class LockReleaseNotify implements Watcher {
	DistributedLock dLock;

	LockReleaseNotify(DistributedLock dLock) {
		this.dLock = dLock;
	}

	@Override
	public void process(WatchedEvent event) {
		if (event.getType().equals(EventType.NodeDeleted)) {
			synchronized (dLock) {
				dLock.notify();
			}
		}
	}
}
