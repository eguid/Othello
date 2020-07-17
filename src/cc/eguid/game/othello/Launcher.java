package cc.eguid.game.othello;

import java.io.File;
import java.util.logging.Logger;

import cc.eguid.game.othello.ctrl.MainController;
import cc.eguid.game.othello.data.GameDataCache;

/**
 * 游戏启动器（游戏入口）
 * 
 * @author eguid
 *
 */
public class Launcher {
	final static Logger log=Logger.getLogger("app");
	
	final static MainController ctrl = new MainController();

	public static void main(String[] args) throws InterruptedException {
		System.out.println("用户的账户名称:"+System.getProperty("user.name"));
		System.out.println("用户的主目录:"+System.getProperty("user.home"));
		System.out.println("用户的当前工作目录:"+System.getProperty("user.dir"));
		System.out.println("当前的classpath的绝对路径的URI表示法:" + Thread.currentThread().getContextClassLoader().getResource("").getPath());
		System.out.println("得到的是当前的classpath的绝对URI路径:"+ Launcher.class.getResource("/").getPath());
		System.out.println("得到的File的绝对URI路径:"+ new File("").getAbsolutePath());

		log.info("启动游戏");
		Thread.currentThread().setName("游戏启动器");
		if (ctrl != null) {
			
			GameDataCache.addData("Main", ctrl);// 存放主控制器
			ctrl.init("eguid黑白棋",640,640,8);
			log.info("进入游戏");
			ctrl.start();// 开始游戏
//			ctrl.stop();

		}
	}

}
