package concurrency;

import java.util.HashMap;

public class Demo1 {
	private HashMap<String, String> map = new HashMap<>();

	public void put(String key, String value) {
		synchronized (this) {
			map.put(key, value);
		}
	}

	public String get(String key) {
		synchronized (this) {
			return map.get(key);
		}
	}
}
