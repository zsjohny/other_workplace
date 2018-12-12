package javatest;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class JAVA7Test {

	public static void main(String[] args) throws InterruptedException {

		Lock lock = new ReentrantLock();

		Condition condition = lock.newCondition();

		new Thread(new Runnable() {

			@Override
			public void run() {

				lock.lock();
				System.out.println("into thread1 lock");
				try {
					condition.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				System.out.println("continue thread1 dosomething");
				lock.unlock();
			}
		}).start();

		new Thread(new Runnable() {

			@Override
			public void run() {

				lock.lock();
				System.out.println("accept thread2 lock");


				System.out.println(" thread2 dosomething");

				// condition.signalAll();
				lock.unlock();

			}
		}).start();


	}

}
