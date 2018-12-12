package javatest;


import java.security.NoSuchAlgorithmException;
import java.util.concurrent.atomic.LongAdder;

public class LongAdderTest {

	public static void main(String[] args) throws InterruptedException, NoSuchAlgorithmException {

		LongAdder adder = new LongAdder();
		
		

		new Thread(() -> adder.add(1)).start();

		new Thread(() -> adder.add(1)).start();



		System.out.println(adder.intValue());


		
	}

}
