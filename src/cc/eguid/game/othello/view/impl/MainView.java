package cc.eguid.game.othello.view.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import cc.eguid.game.othello.ctrl.OthelloAlgorithm;
import cc.eguid.game.othello.data.GameDataCache;
import cc.eguid.game.othello.data.MainViewData;
import cc.eguid.game.othello.obj.Chess;
import cc.eguid.game.othello.obj.ChessColor;
import cc.eguid.game.othello.obj.Chessboard;
import cc.eguid.game.othello.view.ViewTemplate;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 主界面
 * 
 * @author eguid
 *
 */
public class MainView extends ViewTemplate {
	final Logger log=Logger.getLogger("游戏主界面");
	private Scene scene;
	/**
	 * 棋盘和棋子画板
	 */
	private Canvas boardCanvas, chessCanvas;
	GraphicsContext gc,gc2;
	private Text playtext;//当前执棋
	private Text postionFlagText;//落子位置
	private Text revFlagText;//翻转数量
	private Text whiteNumText;//白棋数量
	private Text blackNumText;//黑棋数量
	private Text dropFlagText;//可落子数量
	private Text winnerText; //赢得了胜利
	
	Chessboard chessboard;// 棋盘

	String title;// 标题

	int width, height;// 界面的宽高
	
	int startX=0,startY=0;
	
	OthelloAlgorithm othelloAlgorithm;
	
	public MainView() {// 实例化
		MainViewData data = GameDataCache.getData("MainViewData");
		chessboard = data.getChessboard();
		title = data.getName();
		width = data.getWidth();
		height = data.getHeight();
		//获取算法
		othelloAlgorithm=GameDataCache.getData("OthelloAlgorithm");
	}
	
	/**
	 * 创建界面病初始化界面元素
	 */
	private void initView() {
		Font font=new Font(30),font2=new Font(20);
		Text numText=new Text(825,35,"双方局势");
		numText.setFont(font2);
		
		Text blackText=new Text(750,105,"黑棋数");
		blackText.setFont(font2);
		
		blackNumText=new Text(770,165,"2");
		blackNumText.setFont(font);
		
		Text text=new Text(850,165,"vs");
		text.setFont(font);
		
		Text whiteText=new Text(925,105,"白棋数");
		whiteText.setFont(font2);
		
		whiteNumText=new Text(945,165,"2");
		whiteNumText.setFont(font);
		
		Text cuChessText=new Text(750,300,"当前执棋方");
		cuChessText.setFont(font2);
		
		playtext = new Text(920, 300, "黑棋");
		playtext.setFont(font);
		
		Text postionText=new Text(750,400,"最后落子位置");
		postionText.setFont(font2);
		
		postionFlagText=new Text(920,400,"暂无");
		postionFlagText.setFont(font);
		
		Text revText=new Text(750,500,"翻转棋子数量");
		revText.setFont(font2);
		
		revFlagText=new Text(920,500,"无");
		revFlagText.setFont(font);
		
		Text dropText=new Text(750,600,"可落子位置数量");
		dropText.setFont(font2);
		
		dropFlagText=new Text(920,600,"无");
		dropFlagText.setFont(font);
		
		winnerText=new Text(700,240,"");//赢棋提示
		winnerText.setFont(new Font(40));
		winnerText.setDisable(true);
		
		Group root = new Group(playtext,postionText,cuChessText,postionFlagText,revText,revFlagText,text,numText, blackText,blackNumText,whiteText,whiteNumText,dropText,dropFlagText,winnerText);
		boardCanvas = new Canvas(chessboard.getPixelWidth(),chessboard.getPixelHeight());
		gc = boardCanvas.getGraphicsContext2D();
		//初始化绘制棋盘
		drawBoard();
		root.getChildren().add(boardCanvas);
		
		chessCanvas = new Canvas(chessboard.getPixelWidth(),chessboard.getPixelHeight());
		gc2 = chessCanvas.getGraphicsContext2D();
		gc2.setFill(Color.GREEN);
		gc2.setStroke(Color.BLUE);
		//初始化绘制棋子
		initChesss();

		// 监听鼠标点击事件
		chessCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			
			// 棋子画板鼠标点击处理
			@Override
			public void handle(MouseEvent event) {
				 int x= (int)event.getX();
				 int y = (int)event.getY();
				 log.info("鼠标点击坐标:"+x+","+y);
				 //点击是否在棋盘内 
				 if(inBounds(x,y)) {
					 ChessColor cuColor=othelloAlgorithm.getCuColor();//当前落子棋子颜色
					 int cellX=convertX(x);
					 int cellY=convertY(y);
					 //如果有翻转的棋子，棋子颜色就会变化
					 List<Chess> needchessmans = othelloAlgorithm.reversing(cellX,cellY);
					 //当前落子的棋子
					 Chess chess=othelloAlgorithm.getLastChess();
					 int size=0;
					 if (needchessmans!=null&&(size=needchessmans.size())> 0) {
							drawChess(chess);// 绘制棋子
							for (Chess man : needchessmans) {
								log.info("设置颜色"+chess.getColor());
								man.setColor(cuColor);
								drawChess(man);// 绘制棋子
							}
							updateState(chess,size);
							
							if(othelloAlgorithm.isOver()) {//游戏是否结束
								chessCanvas.setDisable(true);
								int blackNum=othelloAlgorithm.getCurrentBlackChessNum();
								int whiteNum=othelloAlgorithm.getCurrentWhiteChessNum();
								String winner=whiteNum>blackNum?"白棋":"黑棋";
								String winnerStr="恭喜"+winner+"赢得胜利！";
								winnerText.setText(winnerStr);
								winnerText.setDisable(false);
								if(confirmDialog(winnerStr,"是否开始新一局游戏？")) {
									restart();//重新开始
								}
							}
					 }else {
						 needchessmans=null;
					 }
				 }
			}

		});
		root.getChildren().add(chessCanvas);
		scene = new Scene(root,width,height);
	}

	/**
	 * 落子后更新界面各属性状态
	 * @param chess
	 * @param size
	 */
	private void updateState(Chess chess,int size) {
		int blackNum=othelloAlgorithm.getCurrentBlackChessNum();
		int whiteNum=othelloAlgorithm.getCurrentWhiteChessNum();
		if(chess==null) {
			postionFlagText.setText("空");
		}else {
			postionFlagText.setText("["+(chess.getX()+1)+","+(chess.getY()+1)+"]");
		}
		
		revFlagText.setText(""+size);
		
		blackNumText.setText(""+blackNum);
		whiteNumText.setText(""+whiteNum);
		forecast();//预测落子点
		ChessColor cuColor=othelloAlgorithm.getCuColor();//当前落子棋子颜色
		//下子后切换棋子颜色
		playtext.setText(cuColor==ChessColor.white?"白棋":"黑棋");
		dropFlagText.setText(""+othelloAlgorithm.getCanDropSize());
	}
	
	/**
	 * 重新开始
	 */
	private void restart() {
		othelloAlgorithm.restart();
		initChesss();
		forecast();
		winnerText.setText("");
		updateState(null,0);
		chessCanvas.setDisable(false);
	}
	/**
	 * 初始化绘制棋子
	 */
	private void initChesss(){
		gc2.clearRect(0, 0, width, height);
		Chess[][] chessList =othelloAlgorithm.getChesss();
		// 绘制初始四枚棋子
		if (chessList != null) {
			for (Chess[] mans : chessList) {
				if(mans==null) {
					continue;
				}
				for(Chess man:mans) {
					if(man==null) {
						continue;
					}
					drawChess(man);
				}
			}
		}
	}
	@Override
	public void init() throws Exception {
		//初始化操作
		initView();//创建界面元素
	}

	/**
	 * 绘制棋盘
	 * @param gc
	 */
	private void drawBoard() {
		gc.setLineWidth(chessboard.getLineThick());
		//填充棋盘颜色
		gc.setFill(chessboard.getBackgroud());
		int axisNum = chessboard.getCellSize();
		gc.fillRect(0, 0, chessboard.getPixelWidth(), chessboard.getPixelHeight());
		gc.setFill(new Color(70.0/255, 70.0/255,70.0/255, 1.0));
		//绘制棋盘格子
		for (int i = 0; i <= axisNum; i++) {
			int[] x1y1 = getXY(0, i);
			int[] x2y2 = getXY(axisNum, i);
			gc.strokeLine(x1y1[0], x1y1[1], x2y2[0], x2y2[1]);
			gc.strokeLine(x1y1[1], x1y1[0], x2y2[1], x2y2[0]);
		}
	}
	
	/**
	 * 是否在棋盘边界范围之内
	 * 
	 * @return
	 */
	public boolean inBounds(int x, int y) {
		int sumW=chessboard.getPixelWidth();
		int sumH=chessboard.getPixelHeight();
		if (x<= startX ||x >= sumW || y<= startY|| y >= sumH) {
			return false;
		}
		return true;
	}
	
	/**
	 * 格子坐标转换为像素坐标
	 * 
	 * @param x
	 *            -格子坐标
	 * @param y
	 *            -格子坐标
	 * @return
	 */
	private int[] getXY(int x, int y) {
		int[] xy = {x * chessboard.getCellWidth(), y * chessboard.getCellHeight() };
		return xy;
	}
	
	/**
	 * 转换x坐标为数组序号
	 * 
	 * @param x
	 * @return
	 */
	public int convertX(int x) {
		return (x - startX) / chessboard.getCellWidth();
	}

	/**
	 * 转换y坐标为数组序号
	 * 
	 * @param y
	 * @return
	 */
	public int convertY(int y) {
		return (y - startY) / chessboard.getCellHeight();
	}

	/**
	 * 绘制棋子
	 * 
	 * @param gc
	 */
	public void drawChess(Chess chess) {
		int width = chessboard.getCellWidth();
		int height = chessboard.getCellHeight();
		Color color=ChessColor.white == chess.getColor() ? Color.WHITE : Color.BLACK;
		gc2.setFill(color);
		gc2.fillOval(chess.getX()*width, chess.getY()*height, width, height);
	}
	
	/**
	 * 绘制可落子位置标注
	 * @param gc
	 * @param chess
	 */
	public void drawMarker(Chess chess) {
		int width = chessboard.getCellWidth();
		int height = chessboard.getCellHeight();
		gc.setFill(Color.ALICEBLUE);
		gc.setGlobalAlpha(0.15);
		gc.fillOval(chess.getX()*width, chess.getY()*height, width, height);
		gc.setGlobalAlpha(1);
	}
	
	/**
	 * 批量绘制落子位置提示标注
	 * @param gc
	 * @param c
	 */
	public void drawMarkers(Iterator<Chess> c) {
		for(;c.hasNext();) {
			Chess s=c.next();
			drawMarker(s);
		}
	}
	
	/**
	 * 计算可落子位置并绘制
	 * @param gc
	 */
	public void forecast() {
		drawBoard();
		//落子后计算当前棋色可落子位置
		Collection<Chess> droplist=othelloAlgorithm.forecast();
		//如果预测点为空，则再次切换执棋方
		if(droplist!=null) {
			drawMarkers(droplist.iterator());
		}
	}

	
	@Override
	protected Scene getScene() {
		return this.scene;
	}

	@Override
	protected String getTitle() {
		return this.title;
	}

	@Override
	public void load() {
		forecast();
	}

	
}
