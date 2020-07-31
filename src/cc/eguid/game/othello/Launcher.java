package cc.eguid.game.othello;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.logging.Logger;

import cc.eguid.game.othello.data.GameDataCache;
import cc.eguid.game.othello.data.MainViewData;
import cc.eguid.game.othello.scene.ChooseModeScene;

/**
 * 游戏启动器（游戏入口）
 * 
 * @author eguid
 *
 */
public class Launcher {
	final static Logger log=Logger.getLogger("app");

	public static void main(String[] args) throws InterruptedException {
		log.info("启动游戏");
		Thread.currentThread().setName("游戏启动器");
		//初始化一些数据
		init("eguid黑白棋",640,640,8);
		
		Properties prop=LaunchUtil.getPropertiesFromCuPath("conf.properties");
		for(Entry<Object, Object> en:prop.entrySet()) {
			GameDataCache.addData((String)en.getKey(), (String)en.getValue());
		}
		
		try {
			LaunchUtil.launch(ChooseModeScene.class,prop);
		} catch (ClassNotFoundException e) {
		}
		
	}
	
	public static void init(String title, int width, int height, int cellSize) {
		MainViewData mainViewData = new MainViewData(title, width, height, cellSize, width / cellSize, height / cellSize);
		GameDataCache.addData("MainViewData", mainViewData);
	}


}
