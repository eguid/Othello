package cc.eguid.game.othello.ctrl;

import cc.eguid.game.othello.data.GameDataCache;
import cc.eguid.game.othello.data.MainViewData;
import cc.eguid.game.othello.obj.Chessboard;
import cc.eguid.game.othello.view.impl.MainView;
import javafx.application.Application;
import javafx.scene.paint.Color;

/**
 * 主程序控制
 * 
 * @author eguid
 *
 */
public class MainController {

	public static Thread mainViewThread;
	public static Thread promptViewThread;
	
	public OthelloAlgorithm othelloAlgorithm;
	
	public Thread getMainViewThread() {
		return mainViewThread;
	}

	/**
	 * 初始化一些游戏数据参数
	 * 
	 * @return
	 */
	public boolean init() {
		int cellSize=8;
		int cellPixelSize=80;
		int width=640,height=640;
		Chessboard chessboard =new Chessboard(cellSize, cellPixelSize,cellPixelSize,width,height, Color.RED, 1, Color.DARKSEAGREEN);
		MainViewData mainViewData=new MainViewData("黑白棋", chessboard,1080,640);
		GameDataCache.addData("MainViewData", mainViewData);
		int[] startpoint={0,0};
		othelloAlgorithm = new OthelloAlgorithm(startpoint, cellSize,width,height);
		GameDataCache.addData("OthelloAlgorithm", othelloAlgorithm);
		return true;
	}

	/**
	 * 开始游戏
	 * 
	 * @return
	 */
	public synchronized boolean start() {
		if(mainViewThread==null) {
			craeteMainView();
			mainViewThread.start();
			return true;
		}
		return false;
	}
	
	/*
	 * 创建游戏主界面
	 */
	private static void craeteMainView() {
		//用于启动游戏主程序
		mainViewThread=new Thread() {
			public void run() {
				startPage(MainView.class);
			};
		};
		mainViewThread.setName("游戏主界面");
	}
	
	/**
	 * 启动页面
	 * @param appClass
	 */
	public static void startPage(Class<? extends Application> appClass) {
		Application.launch(appClass);
	}

	/**
	 * 重新开始
	 * 
	 * @return
	 */
	public boolean restart() {
		if(init()) {
			return start();
		}
		return false;
	}

	public void stop() {
		if(mainViewThread!=null) {
			mainViewThread.stop();
		}
	}
	
	public void exit() {
		System.exit(2);
	}
	
}
