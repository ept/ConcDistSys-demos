package concurrency;

import java.util.HashMap;

public class Demo2 {
	private HashMap<String, String> map = new HashMap<>();

	public synchronized void put(String key, String value) {
		map.put(key, value);
	}

	public synchronized String get(String key) {
		return map.get(key);
	}
}
