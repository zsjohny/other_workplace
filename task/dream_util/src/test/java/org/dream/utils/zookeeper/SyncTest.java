/**
 * 
 */
package org.dream.utils.zookeeper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Boyce
 *
 */
public class SyncTest {
	public static void main(String[] args) {
		final Lock lock = new ReentrantLock();
		
		new Thread(){
			public void run() {
				synchronized (lock) {
					System.out.println("wait 1");
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("unlock 1");
				}
				
			};
		}.start();
		new Thread(){
			public void run() {
				synchronized (lock) {
					System.out.println("notify 1");
						lock.notify();
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					System.out.println("unlock 2");
				}
				
			};
		}.start();
		new Thread(){
			public void run() {
				synchronized (lock) {
					System.out.println("unlock 3");
				}
				
			};
		}.start();
		new Thread(){
			public void run() {
				synchronized (lock) {
					System.out.println("unlock 4");
				}
				
			};
		}.start();
	}

}
