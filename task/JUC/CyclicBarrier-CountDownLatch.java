package javatest;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class JAVA7Test {


	public static void main(String[] args) throws InterruptedException {

		// CyclicBarrier 表示所有的都在等待 所有的线程的到来，一旦到来，在启动所有的线程任务

		CyclicBarrier cyclicBarrier = new CyclicBarrier(5);


		for (int i = 0; i < 4; i++) {

			final int b = i;
			new Thread(new Runnable() {
				public void run() {
					System.out.println(b + "into,,,,");
					try {
						cyclicBarrier.await();
					} catch (InterruptedException | BrokenBarrierException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(b + "end,,,,");

				}
			}).start();


		}

		// CountDownLatch 主要表示是一个或者多个线程在等待其他线程进行操作后再继续进行事情的处理
		
		// int executorSize=10;
		//
		// ExecutorService
		// executorService=Executors.newFixedThreadPool(executorSize);
		//
		// CountDownLatch startLatch=new CountDownLatch(1);
		//
		// CountDownLatch enDownLatch = new CountDownLatch(executorSize);
		//
		// for (int i = 0; i < executorSize; i++) {
		//
		// executorService.execute(new Runnable() {
		//
		// @Override
		// public void run() {
		// try {
		// startLatch.await();
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// System.out.println("do some thing");
		// enDownLatch.countDown();
		//
		// System.out.println(enDownLatch.getCount());
		//
		//
		// }
		// });
		//
		// }
		//
		//
		// System.out.println("start...");
		//
		// startLatch.countDown();
		//
		// enDownLatch.await();
		//
		// System.out.println("end....");
		// executorService.shutdown();
		
		
		
		

	}


}
