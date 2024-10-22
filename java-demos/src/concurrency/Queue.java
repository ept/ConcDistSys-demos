package concurrency;

public class Queue {
	private Object[] buffer;
	private int in = 0, out = 0;

	public Queue(int capacity) {
		this.buffer = new Object[capacity];
	}

	public synchronized void produce(Object item)
			throws InterruptedException {
		while ((in + 1) % buffer.length == out) {
			this.wait();
		}
		buffer[in] = item;
		in = (in + 1) % buffer.length;
		this.notifyAll();
	}

	public synchronized Object consume()
			throws InterruptedException {
		while (in == out) {
			this.wait();
		}
		Object item = buffer[out];
		out = (out + 1) % buffer.length;
		this.notifyAll();
		return item;
	}
}
