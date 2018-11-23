package cc.eguid.game.othello.data;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程内部复用数据
 * @author eguid
 *
 */
public class LocalThreadData {

	final static ThreadLocal<Map<String, Object>> localdata = new ThreadLocal<Map<String, Object>>() {
		@Override
		protected Map<String, Object> initialValue() {
			return new HashMap<>();
		}
	};

	@SuppressWarnings("unchecked")
	public static <T> T get(String key) {
		return (T) localdata.get().get(key);
	}

	public static void set(String key, Object obj) {
		localdata.get().put(key, obj);
	}
}
