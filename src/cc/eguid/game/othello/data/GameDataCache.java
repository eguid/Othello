package cc.eguid.game.othello.data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 游戏数据缓存
 * @author eguid
 *
 */
public class GameDataCache {
	
	static Map<String, Object> dataMap=new ConcurrentHashMap<>(100);
	
	/**
	 * 获取数据
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public final static <T> T getData(String key) {
		return (T)dataMap.get(key);
	}
	
	/**
	 * 存放游戏数据
	 * @param key
	 * @param value
	 */
	public final static void addData(String key,Object value) {
		dataMap.put(key, value);
	}
}
