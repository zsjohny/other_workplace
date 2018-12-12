package javatest;

import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

public class WorkStealing extends RecursiveTask<Integer> {


	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException, ExecutionException {

		// 使用它 线程能被释放

		AtomicInteger integer = new AtomicInteger(1);

		ExecutorService service = null;
		while (integer.get() <= 10) {
			long start = System.currentTimeMillis();

			LongAdder adder = new LongAdder();


			service = Executors.newWorkStealingPool();

			Future<Long> task = service.submit(() -> {

				for (int i = 0; i < 10000000; i++) {
					adder.increment();

				}
				integer.incrementAndGet();
				return adder.longValue();

			});
			Thread.sleep(2000);
			System.out.println(task.get());

			System.out.println(System.currentTimeMillis() - start);
			System.err.println(integer.get());



		}

		System.out.println("end....");

		service.shutdownNow();

	}

	@Override
	protected Integer compute() {
		return null;
	}

}
