package cc.eguid.game.othello.scene;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import cc.eguid.game.othello.LaunchUtil;
import cc.eguid.game.othello.ai.MachineAiCaculate;
import cc.eguid.game.othello.ctrl.OthelloAlgorithm;
import cc.eguid.game.othello.data.GameDataCache;
import cc.eguid.game.othello.data.MainViewData;
import cc.eguid.game.othello.obj.Chess;
import cc.eguid.game.othello.obj.ChessColor;
import cc.eguid.game.othello.obj.Chessboard;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 人机对战场景
 * @author eguid
 *
 */
public class ManMachineBattleScene extends ViewTemplate {

	final Logger log = Logger.getLogger("游戏主界面");

	/**
	 * 棋盘和棋子画板
	 */
	private Canvas boardCanvas, chessCanvas;
	GraphicsContext gc, gc2;
	private Text playtext;// 当前执棋
	private Text postionFlagText;// 落子位置
	private Text revFlagText;// 翻转数量
	private Text whiteNumText;// 白棋数量
	private Text blackNumText;// 黑棋数量
	private Text dropFlagText;// 可落子数量
	private Text winnerText; // 赢得了胜利

	Chessboard chessboard;// 棋盘

	// 音效
	AudioClip dropChessAC, winAC;// 下棋音效和胜利音效
	MediaPlayer gbAC;// 背景音乐

	OthelloAlgorithm othelloAlgorithm;
	
	MachineAiCaculate mac;//电脑Ai
	
	int handChessFlag=0;//当前执棋方，0-表示玩家，1-表示电脑正在下棋
	
	boolean gameOver=false;
	
	@Override
	public void load() {
		initWindow("奥赛罗（黑白棋） - 人机对战模式 - eguid作品", 1100, 680);
		// 初始化操作
		initView();// 创建界面元素
	}
	
	@Override
	protected Scene initScene() {
		return new Scene(new Group(), 600, 600);
	}

	/**
	 * 创建界面病初始化界面元素
	 */
	private void initView() {
		MainViewData data = GameDataCache.getData("MainViewData");
		othelloAlgorithm = data.getOthelloAlgorithm();
		chessboard = data.getChessboard();
		String dropChess = "file://" + LaunchUtil.getCuPath() + GameDataCache.getData("dropChess");
		String background = "file://" + LaunchUtil.getCuPath() + GameDataCache.getData("background");
		String win = "file://" + LaunchUtil.getCuPath() + GameDataCache.getData("win");

		mac=new MachineAiCaculate(othelloAlgorithm);
		
		log.info("背景音乐：" + background + "，下棋音效：" + dropChess + "，胜利音效：" + win);

		dropChessAC = new AudioClip(dropChess);
		winAC = new AudioClip(win);
		// 背景音效
		gbAC = new MediaPlayer(new Media(background));
		gbAC.setAutoPlay(true);
		gbAC.setVolume(1.0);
		gbAC.setCycleCount(Integer.MAX_VALUE);
		gbAC.play();

		Font font = new Font(30), font2 = new Font(20);
		Text numText = new Text(825, 35, "双方局势");
		numText.setFont(font2);

		Text blackText = new Text(750, 105, "黑棋数");
		blackText.setFont(font2);

		blackNumText = new Text(770, 165, "2");
		blackNumText.setFont(font);

		Text text = new Text(850, 165, "vs");
		text.setFont(font);

		Text whiteText = new Text(925, 105, "白棋数");
		whiteText.setFont(font2);

		whiteNumText = new Text(945, 165, "2");
		whiteNumText.setFont(font);

		Text cuChessText = new Text(750, 300, "当前执棋方");
		cuChessText.setFont(font2);

		playtext = new Text(920, 300, "黑棋");
		playtext.setFont(font);

		Text postionText = new Text(750, 400, "最后落子位置");
		postionText.setFont(font2);

		postionFlagText = new Text(920, 400, "暂无");
		postionFlagText.setFont(font);

		Text revText = new Text(750, 500, "翻转棋子数量");
		revText.setFont(font2);

		revFlagText = new Text(920, 500, "无");
		revFlagText.setFont(font);

		Text dropText = new Text(750, 600, "可落子位置数量");
		dropText.setFont(font2);

		dropFlagText = new Text(920, 600, "无");
		dropFlagText.setFont(font);

		winnerText = new Text(700, 240, "");// 赢棋提示
		winnerText.setFont(new Font(40));
		winnerText.setDisable(true);

		Group root = new Group(playtext, postionText, cuChessText, postionFlagText, revText, revFlagText, text, numText,
				blackText, blackNumText, whiteText, whiteNumText, dropText, dropFlagText, winnerText);
		addNode(root);
		boardCanvas = new Canvas(chessboard.getPixelWidth(), chessboard.getPixelHeight());
		gc = boardCanvas.getGraphicsContext2D();
		// 初始化绘制棋盘
		chessboard.draw(gc);
		addNode(boardCanvas);

		chessCanvas = new Canvas(chessboard.getPixelWidth(), chessboard.getPixelHeight());
		gc2 = chessCanvas.getGraphicsContext2D();
		gc2.setFill(Color.GREEN);
		gc2.setStroke(Color.BLUE);

		// 监听鼠标点击事件
		chessCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				log.info("事件处理：当前线程："+Thread.currentThread().getName());
				
				//如果当前时电脑执棋，则不操作
				if(handChessFlag==1) {
					log.info("电脑正在下棋，请稍等片刻");
					return;
				}
				int x = (int) event.getX(), y = (int) event.getY();
				// 点击是否在棋盘内
				if (chessboard.inBounds(x, y)) {
					//改变执棋方，防止玩家点击
					handChessFlag=1;
					
					// 转换界面坐标为棋盘坐标
					int cellX = convertX(x);
					int cellY = convertY(y);
					ChessColor cuColor=othelloAlgorithm.getCuColor();
					
					// 落子
					dropOneChess(cellX, cellY);
					gameIsOver();
					//玩家落子完，让Ai下
					//ai落子，如果一直不可落子
					//轮到ai落子
					//如果两次棋子颜色相同，还是玩家下棋
					
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
							}
							Platform.runLater(new Task<Integer>() {
								@Override
								protected Integer call() throws Exception {
									if(handChessFlag==1) {
										//等待片刻
										for(int i=60;i>0;i--) {
											if(cuColor.equals(othelloAlgorithm.getCuColor())) {
												break;
											}
											aiDropOneChess();
											
										}
										
										handChessFlag=0;
									}
									gameIsOver();
									return 1;
								}
								
							});
						}
					}).start();
					
				}
				
			}
		});
		addNode(chessCanvas);
		// 开始游戏
		restart();
	}

	/**
	 * ai下棋
	 * @return 
	 */
	private void aiDropOneChess() {
		int cellX = -1, cellY = -1;
		//用户点击后立刻让电脑进行操作
		Chess dropChess=mac.caculate();
		if(dropChess!=null) {
			cellX=dropChess.getX();
			cellY=dropChess.getY();
			dropOneChess(cellX, cellY);
		}
	}
	
	/**
	 * 落子
	 * @param cellX
	 * @param cellY
	 */
	private void dropOneChess(int cellX, int cellY) {
		ChessColor cuColor = othelloAlgorithm.getCuColor();// 当前落子棋子颜色
		
		// 如果有翻转的棋子，棋子颜色就会变化
		List<Chess> needchessmans = othelloAlgorithm.reversing(cellX, cellY);
		// 当前落子的棋子
		Chess chess = othelloAlgorithm.getLastChess();
		int size = 0;
		if (needchessmans != null && (size = needchessmans.size()) > 0) {
			// 播放棋子放下的音效
			dropChessAC.play();
			
			// 绘制棋子
			chess.draw(gc2);
			for (Chess man : needchessmans) {
				man.setColor(cuColor);
				man.draw(gc2);
			}

			// 落子后更新界面各项属性状态
			updateState(chess, size);
			
		} else {
			needchessmans = null;
		}
	}

	/**
	 * 游戏是否结束
	 */
	private void gameIsOver() {
		if (othelloAlgorithm.isOver()) {// 游戏是否结束
			winAC.play();
			gameOver=true;
//			chessCanvas.setDisable(true);
			int blackNum = othelloAlgorithm.getCurrentBlackChessNum();
			int whiteNum = othelloAlgorithm.getCurrentWhiteChessNum();
			String winner = whiteNum > blackNum ? "白棋" : "黑棋";
			String winnerStr = "恭喜" + winner + "赢得胜利！";
			winnerText.setText(winnerStr);
			winnerText.setDisable(false);
			gbAC.pause();
			if (gameOver) {
				if(confirmDialog(winnerStr, "是否开始新一局游戏？")) {
					restart();// 重新开始
				}else {
					gbAC.stop();
					// 打开选择界面
					switchWindow(new ChooseModeScene());
				}
			}
			gameOver=false;
		}
		winAC.stop();
	}

	/**
	 * 计算可落子位置并绘制
	 * 
	 * @param gc
	 */
	private void forecast() {
		chessboard.draw(gc);
		// 落子后计算当前棋色可落子位置
		Collection<Chess> droplist = othelloAlgorithm.forecast();
		// 如果预测点为空，则再次切换执棋方
		if (droplist != null) {
			drawMarkers(droplist.iterator());
		}
	}

	/**
	 * 落子后更新界面各属性状态
	 * 
	 * @param chess
	 * @param size
	 */
	private void updateState(Chess chess, int size) {
		int blackNum = othelloAlgorithm.getCurrentBlackChessNum();
		int whiteNum = othelloAlgorithm.getCurrentWhiteChessNum();
		if (chess == null) {
			postionFlagText.setText("空");
		} else {
			postionFlagText.setText("[" + (chess.getX() + 1) + "," + (chess.getY() + 1) + "]");
		}

		revFlagText.setText("" + size);

		blackNumText.setText("" + blackNum);
		whiteNumText.setText("" + whiteNum);
		forecast();// 预测落子点
		ChessColor cuColor = othelloAlgorithm.getCuColor();// 当前落子棋子颜色
		// 下子后切换棋子颜色
		playtext.setText(cuColor == ChessColor.white ? "白棋" : "黑棋");
		dropFlagText.setText("" + othelloAlgorithm.getCanDropSize());
	}

	/**
	 * 重新开始游戏（等同于开始）
	 */
	private void restart() {
		othelloAlgorithm.restart();
		initChesss();
		forecast();
		winnerText.setText("");
		updateState(null, 0);
		chessCanvas.setDisable(false);
		gbAC.play();
	}

	/**
	 * 初始化绘制棋子
	 */
	private void initChesss() {
		gc2.clearRect(0, 0, getWidth(), getHeight());
		Chess[][] chessList = othelloAlgorithm.getChesss();
		// 绘制初始四枚棋子
		if (chessList != null) {
			for (Chess[] mans : chessList) {
				if (mans == null) {
					continue;
				}
				for (Chess man : mans) {
					if (man == null) {
						continue;
					}
					man.draw(gc2);
				}
			}
		}
	}

	/**
	 * 转换x坐标为数组序号
	 * 
	 * @param x
	 */
	private int convertX(int x) {
		return x / chessboard.getCellWidth();
	}

	/**
	 * 转换y坐标为数组序号
	 * 
	 * @param y
	 */
	private int convertY(int y) {
		return y / chessboard.getCellHeight();
	}

	/**
	 * 绘制可落子位置标注
	 * 
	 * @param gc
	 * @param chess
	 */
	private void drawMarker(Chess chess) {
		int width = chessboard.getCellWidth();
		int height = chessboard.getCellHeight();
		gc.setFill(Color.ALICEBLUE);
		gc.setGlobalAlpha(0.15);
		gc.fillOval(chess.getX() * width, chess.getY() * height, width, height);
		gc.setGlobalAlpha(1);
	}

	/**
	 * 批量绘制落子位置提示标注
	 * 
	 * @param gc
	 * @param c
	 */
	private void drawMarkers(Iterator<Chess> c) {
		for (; c.hasNext();) {
			Chess s = c.next();
			drawMarker(s);
		}
	}
}