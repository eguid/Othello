package cc.eguid.game.othello.ctrl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import cc.eguid.game.othello.LaunchUtil;
import cc.eguid.game.othello.data.GameDataCache;
import cc.eguid.game.othello.data.MainViewData;
import cc.eguid.game.othello.scene.MainScene;

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
	
	//棋盘宽度，高度，格子数量，格子宽（等于棋子宽），格子高（等于棋子高）
	Integer width, height,cellSize,cellWidth,cellHeight;
	//标题
	String title;
	public Thread getMainViewThread() {
		return mainViewThread;
	}

	/**
	 * 初始化一些游戏数据参数
	 * 
	 * @return
	 */
	public boolean init(String title,int width,int height,int cellSize) {
		this.title=title;
		this.width=width;
		this.height=height;
		this.cellSize=cellSize;
		this.cellWidth=width/cellSize;
		this.cellHeight=height/cellSize;
		initCtrl();
		return true;
	}
	
	private boolean init() {
		initCtrl();
		return true;
	}

	private void initCtrl() {
		MainViewData mainViewData=new MainViewData(title,width,height,cellSize,cellWidth,cellHeight);
		GameDataCache.addData("MainViewData", mainViewData);
		int[] startpoint={0,0};
		othelloAlgorithm = new OthelloAlgorithm(startpoint, cellSize,width,height,cellWidth,cellHeight);
		GameDataCache.addData("OthelloAlgorithm", othelloAlgorithm);
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
				Properties prop=new Properties();
				try {
					String conf=LaunchUtil.getCuPath()+"conf.properties";
					System.err.println("配置文件地址："+conf);
					prop.load(new FileInputStream(new File(conf)));
					LaunchUtil.launch(MainScene.class,prop);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			};
		};
		mainViewThread.setName("游戏主界面");
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
			mainViewThread.interrupt();
		}
	}
	
	public void exit() {
		System.exit(2);
	}
	
}
