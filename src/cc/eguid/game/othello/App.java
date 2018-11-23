package cc.eguid.game.othello;

import java.util.logging.Logger;

import cc.eguid.game.othello.ctrl.MainController;
import cc.eguid.game.othello.data.GameDataCache;

/**
 * 游戏启动入口
 * 
 * @author eguid
 *
 */
public class App {
	final static Logger log=Logger.getLogger("app");
	
	final static MainController ctrl = new MainController();

	public static void main(String[] args) throws InterruptedException {
		Thread.currentThread().setName("游戏启动器");
		if (ctrl != null) {
			GameDataCache.addData("Main", ctrl);// 存放主控制器
			if (ctrl.init()) {// 加载一些数据
				log.info("启动游戏");
				ctrl.start();// 开始游戏
//				ctrl.stop();
			}
			boolean status=true;
			for(;status;Thread.sleep(10000)) {
				Thread t=ctrl.getMainViewThread();
				if(t!=null) {
					status=t.isAlive();
					log.info("游戏状态【主界面："+(status?"正在运行":"已停止")+"】");
				}
			}
		}
	}

}
