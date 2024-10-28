package concurrency;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

public class QueueDeadlock {

	private static class PingPongThread implements Runnable {
		private final ArrayBlockingQueue<String> in, out;

		public PingPongThread(ArrayBlockingQueue<String> in, ArrayBlockingQueue<String> out) {
			this.in = in;
			this.out = out;
		}

		@Override
		public void run() {
			int i = 0;
			try {
				while (!Thread.interrupted()) {
					String item = in.take();
					out.put(item);
					System.out.println("Item " + i + " is " + item);
					i++;
				}
			} catch (InterruptedException e) {
				// thread stops executing
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		ArrayBlockingQueue<String> queue1 = new ArrayBlockingQueue<>(5);
		ArrayBlockingQueue<String> queue2 = new ArrayBlockingQueue<>(5);
		new Thread(new PingPongThread(queue1, queue2)).start();
		new Thread(new PingPongThread(queue2, queue1)).start();

		try (Scanner keyboard = new Scanner(System.in)) {
			System.out.println("Enter items to add to the queue");
			while (true) {
				String input = keyboard.nextLine();
				queue1.put(input);
			}
		}
	}
}
