package concurrency;

import java.util.HashSet;

public class QueueTest {
	public static final int NUM_THREADS = 10;
	public static final int ITEMS_PER_THREAD = 10000;

	private static class ProducerThread implements Runnable {
		private final Queue queue;
		private final int id;

		public ProducerThread(Queue queue, int id) {
			this.queue = queue;
			this.id = id;
		}

		@Override
		public void run() {
			try {
				for (int i = 0; i < ITEMS_PER_THREAD; i++) {
					queue.produce(NUM_THREADS * i + this.id);
				}
			} catch (InterruptedException e) {
				// thread stops executing
			}
		}
	}

	private static class ConsumerThread implements Runnable {
		private final Queue queue;
		private final HashSet<String> itemsSeen = new HashSet<>();

		public ConsumerThread(Queue queue) {
			this.queue = queue;
		}

		@Override
		public void run() {
			try {
				while (!Thread.interrupted()) {
					itemsSeen.add(queue.consume().toString());
				}
			} catch (InterruptedException e) {
				// thread stops executing
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		Queue queue = new Queue(10);
		ProducerThread[] producers = new ProducerThread[NUM_THREADS];
		ConsumerThread[] consumers = new ConsumerThread[NUM_THREADS];

		for (int i = 0; i < NUM_THREADS; i++) {
			producers[i] = new ProducerThread(queue, i);
			consumers[i] = new ConsumerThread(queue);
			new Thread(producers[i]).start();
			new Thread(consumers[i]).start();
		}

		Thread.sleep(1000);

		int sum = 0;
		for (int i = 0; i < NUM_THREADS; i++) {
			sum += consumers[i].itemsSeen.size();
		}
		assert sum == NUM_THREADS * ITEMS_PER_THREAD;
		System.out.println(sum);
	}
}
