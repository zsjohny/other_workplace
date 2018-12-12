/**
 * 
 */
package org.dream.utils.zookeeper;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Boyce
 *
 */
public class LockTest {
	public static void main(String[] args) {
		final Lock lock = new ReentrantLock();
		final Condition c1 = lock.newCondition();
		final Condition c2 = lock.newCondition();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("wait c1");
				try {
					c1.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("t1 unlock");
				lock.unlock();
			};
		}.start();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("will signal c1");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("signal c1");
				c1.signal();
				System.out.println("t2 unlock");
				lock.unlock();
			};
		}.start();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("wait c2");
				try {
					c2.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("t3 unlock");
				lock.unlock();
			};
		}.start();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("will signal c2");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("signal c2");
				c2.signal();
				System.out.println("t4 unlock");
				lock.unlock();
			};
		}.start();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("t5 unlock");
				lock.unlock();
			};
		}.start();
		new Thread() {
			public void run() {
				lock.lock();
				System.out.println("t6 unlock");
				lock.unlock();
			};
		}.start();
	}

}
