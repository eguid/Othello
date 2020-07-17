package cc.eguid.game.othello;

import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import javafx.application.Application;

/**
 * javafx启动工具
 * 
 * @author eguid
 *
 */
public class LaunchUtil {

	private final static String prefix = "--";
	private final static String suffix = "=";

	public final static void launch(String className, List<String> params) throws ClassNotFoundException {
		launch(className, (Collection<String>) params);
	}

	public final static void launch(Class<? extends Application> appClass, List<String> params)
			throws ClassNotFoundException {
		launch(appClass, (Collection<String>) params);
	}

	public final static void launch(String className, Set<String> params) throws ClassNotFoundException {
		launch(className, (Collection<String>) params);
	}

	public final static void launch(Class<? extends Application> appClass, Set<String> params) throws ClassNotFoundException {
		launch(appClass, (Collection<String>) params);
	}

	public final static void launch(String className, Collection<String> params) throws ClassNotFoundException {
		Class<? extends Application> cla = findClass(className);
		if (cla != null) {
			launch(cla, params);
		}
	}

	public final static void launch(Class<? extends Application> appClass, Collection<String> params)
			throws ClassNotFoundException {
		String[] arr = (String[]) params.toArray();
		launch(appClass, arr);
	}

	public final static void launch(String className, Properties params) throws ClassNotFoundException {
		launch(className, (Map<?, ?>) params);
	}

	public final static void launch(Class<? extends Application> appClass, Properties params) throws ClassNotFoundException {
		launch(appClass, (Map<?, ?>) params);
	}

	public final static void launch(String className, Map<?, ?> params) throws ClassNotFoundException {
		Class<? extends Application> cla = findClass(className);
		if (cla != null) {
			launch(cla, params);
		}
	}

	public final static void launch(Class<? extends Application> appClass, Map<?, ?> params) throws ClassNotFoundException {
		int index = params.size();
		String[] arrParam = new String[index];
		for (Entry<?, ?> map : params.entrySet()) {
			String param = prefix + map.getKey() + suffix + map.getValue();
			arrParam[--index] = param;
		}
		launch(appClass, arrParam);
	}

	public final static void launch(Class<? extends Application> appClass, String... params) {
		Application.launch(appClass, params);
	}

	@SuppressWarnings("unchecked")
	private final static Class<? extends Application> findClass(String className) throws ClassNotFoundException {
		Class<? extends Application> cla = (Class<? extends Application>) Class.forName(className);
		return cla;
	}

	/**
	 * 获取Application中的参数列表
	 * @param app
	 * @return
	 */
	public final static Map<String, String> getPrams(Application app) {
		Map<String, String> params=app.getParameters().getNamed();
		return params;
	}
	
	/**
	 * 获取Application中的参数
	 * @param app Application
	 * @param key 参数键
	 * @return
	 */
	public final static String getPrams(Application app,String key) {
		Map<String, String> params=getPrams(app);
		if(params!=null) {
			return params.get(key);
		}
		return null;
	}
	
	
	public static String getCuPath() {
		return System.getProperty("user.dir").replace("\\", "/")+"/";
	}
	
	public static String getJarFile(String file) {
		return LaunchUtil.class.getResource(file).getPath();
	}
}
