package cc.eguid.game.othello.ctrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import cc.eguid.game.othello.obj.Chess;
import cc.eguid.game.othello.obj.ChessColor;

/**
 * 奥赛罗游戏规则主算法逻辑
 * 1、预测算法（预测当前执棋方所有落子位置）
 * 2、翻转算法（部分依赖预测算法，例如判断可落子位置）
 * 3、规则逻辑算法（判断输赢、换手、重新开始游戏等，依赖上述两个算法）
 * 
 * @author eguid
 *
 */
public class OthelloAlgorithm {
	final Logger log=Logger.getLogger("游戏主算法逻辑");

	private Chess[][] chesss;

	private int maxSize;// 每行最大方格数

	int chessWidth,chessHeight;//每颗棋子大小
	int width, height;// 宽高

	private Chess[] aroundChess = null;// 当前棋子的周围8颗棋子

	private List<Chess> reverseChess = null;// 可翻转的棋子
	
	private List<Chess> lineReverseChess =null;// 一条直线上可翻转的棋子

	private int currentWhiteChessNum=2;//白棋，初始化有两枚白棋
	private int currentBlackChessNum=2;//黑棋，初始化有两枚黑棋
	private int currentChessNum=4;//当前棋子数量，初始有四枚棋子
	
	
	public ChessColor cuColor=ChessColor.black;//黑棋先手
	public int cuX,cuY;//当前落子位置
	
	/**
	 * 最后一次落子棋子
	 */
	public Chess lastChess;
	
	/**当前可落子棋子位置列表*/
	protected Map<String,Chess> canDropChessList=new Hashtable<>(20);
	/** 当前棋方可落子数量 */
	protected int canDropSize=0;
	
	public boolean isOver=false;//游戏是否结束

	@SuppressWarnings("unused")
	private int temp;
	
	public int getCurrentChessNum() {
		return currentChessNum;
	}

	public void setCurrentChessNum(int currentChessNum) {
		this.currentChessNum = currentChessNum;
	}

	public int getCurrentWhiteChessNum() {
		return currentWhiteChessNum;
	}

	public void setCurrentWhiteChessNum(int currentWhiteChessNum) {
		this.currentWhiteChessNum = currentWhiteChessNum;
	}

	public int getCurrentBlackChessNum() {
		return currentBlackChessNum;
	}

	public void setCurrentBlackChessNum(int currentBlackChessNum) {
		this.currentBlackChessNum = currentBlackChessNum;
	}

	/**
	 * 获取当前可落子位置数量
	 * @return
	 */
	public int getCanDropSize() {
		return canDropSize;
	}

	public void setCanDropChessList(Map<String, Chess> canDropChessList) {
		this.canDropChessList = canDropChessList;
	}
	
	/**
	 * 获得当前可落子位置的棋子
	 * @return
	 */
	public Collection<Chess> getCanDropChessList() {
		return canDropChessList.values();
	}

	/**
	 * 获取当前执棋方颜色
	 * @return
	 */
	public ChessColor getCuColor() {
		return cuColor;
	}

	/**
	 * 切换当前棋子颜色
	 * @return
	 */
	private void changeCuColor() {
		cuColor=(cuColor==ChessColor.white?ChessColor.black:ChessColor.white);
	}
	
	public int getCuX() {
		return cuX;
	}

	public int getCuY() {
		return cuY;
	}
	
	public Chess getLastChess() {
		return lastChess;
	}

	/**
	 * 换手
	 */
	private void changeHands(int x,int y) {
		changeCuColor();
		cuX=x;
		cuY=y;
	}

	public OthelloAlgorithm() {
		super();
	}
	

	/**
	 * 添加棋子（添加到数组，并数量+1），下棋
	 * @param arr1
	 * @param arr2
	 * @param chess
	 */
	public void addChess(Chess chess) {
		chesss[chess.getX()][chess.getY()]=chess;
		currentChessNum++;
		temp=chess.getColor()==ChessColor.white?currentWhiteChessNum++:currentBlackChessNum++;
	}
	
	/**
	 * 删除棋子（从数组中删除，并数量-1），悔棋
	 * @param arr1
	 * @param arr2
	 * @param chess
	 */
	public void removeChess(Chess chess) {
		chesss[chess.getX()][chess.getY()]=null;
		currentChessNum--;
	}
	
	/**
	 * 
	 * @param Chesss 初始化棋子
	 * @param point
	 * @param maxSize
	 * @param width
	 * @param height
	 */
	public OthelloAlgorithm(int[] point, int cellSize, int width, int height,int chessWidth,int chessHeight) {
		super();
		this.maxSize = cellSize;
		this.width = width;
		this.height = height;
		this.chessWidth=chessWidth;
		this.chessHeight=chessHeight;
		start();
	}

	/**
	 * 开始游戏，初始化算法基础数据
	 */
	private void start(){
		isOver=false;
		currentWhiteChessNum=2;//白棋，初始化有两枚白棋
		currentBlackChessNum=2;//黑棋，初始化有两枚黑棋
		currentChessNum=4;//当前棋子数量，初始有四枚棋子
		cuColor=ChessColor.black;//黑棋先手
		initChess(maxSize);
		aroundChess = new Chess[8];// 当前棋子的周围8颗棋子
		reverseChess = new ArrayList<Chess>(maxSize * maxSize);// 可翻转的棋子
		lineReverseChess = new ArrayList<Chess>(maxSize);// 一条直线上可翻转的棋子
	}
	
	/**
	 * 初始化居中四个棋子
	 * @param cellSize
	 */
	private void initChess(int cellSize) {
		if(chesss==null) {
			chesss = new Chess[cellSize][cellSize];//全部棋子
		}else {
			for(Chess[] cs:chesss)
				Arrays.fill(cs, null);
		}
		chesss[cellSize/2][cellSize/2]=new Chess(ChessColor.black,cellSize/2,cellSize/2,chessWidth,chessHeight);
		chesss[cellSize/2][cellSize/2-1]=new Chess(ChessColor.white,cellSize/2,cellSize/2-1,chessWidth,chessHeight);
		chesss[cellSize/2-1][cellSize/2-1]=new Chess(ChessColor.black,cellSize/2-1,cellSize/2-1,chessWidth,chessHeight);
		chesss[cellSize/2-1][cellSize/2]=new Chess(ChessColor.white,cellSize/2-1,cellSize/2,chessWidth,chessHeight);
	}
	
	// 重置当前棋子周围四个方向标识
	public void resetAroundChess() {
		for (int i = 0; i < 8; i++) {
			aroundChess[i] = null;
		}
	}

	public Chess[][] getChesss() {
		return chesss;
	}

	public void setChesss(Chess[][] chesss) {
		this.chesss = chesss;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}

	/**
	 * 检测周围的棋子
	 * 
	 * @return
	 */
	public int findAroundHasChess(int xnum, int ynum) {
		aroundChess[0] = getByArray(xnum, ynum - 1);// 北
		aroundChess[1] = getByArray(xnum + 1, ynum - 1);// 东北
		aroundChess[2] = getByArray(xnum + 1, ynum);// 东
		aroundChess[3] = getByArray(xnum + 1, ynum + 1);// 东南
		aroundChess[4] = getByArray(xnum, ynum + 1);// 南
		aroundChess[5] = getByArray(xnum - 1, ynum + 1);// 西南
		aroundChess[6] = getByArray(xnum - 1, ynum);// 西
		aroundChess[7] = getByArray(xnum - 1, ynum - 1);// 西北
		int nullnum = hasAroundChess(cuColor, aroundChess);
		return 8 - nullnum;
	}

	/**
	 * 获取数组中的对象
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private Chess getByArray(int x, int y) {
		return getObjectByArray(chesss, maxSize, x, y);
	}

	/**
	 * 获取二维数组中的某个对象（并保证不会数组越界，越界返回null）
	 * 
	 * @param arrs
	 * @param maxsize
	 * @param x
	 * @param y
	 * @return
	 */
	private Chess getObjectByArray(Chess[][] arrs, int maxsize, int x, int y) {
		if (x < 0 || y < 0 || x >= maxsize || y >= maxsize) {
			return null;
		}
		return arrs[x][y];
	}

	/**
	 * 周围是否有棋子（其中没有一个为空，则返回-1）
	 * 
	 * @param obj
	 * @return 返回空的数量
	 */
	private int hasAroundChess(ChessColor color, Chess... obj) {
		int index = 0;
		if (obj!=null&&obj.length > 0) {
			for (Chess s : obj) {
				if (s==null||color == s.getColor()) {
					index++;
				}
			}
		}
		return index;
	}

	// 不存在区域外的情况，直接计算即可

	/**
	 * 当前位置是否存在棋子
	 * 
	 * @param chess
	 * @return
	 */
	public boolean hasChess(int x, int y) {
		return getByArray(x, y)!=null;
	}

	/**
	 * 查找当前棋可落子点,与forecast()相同
	 * 
	 * @see forecast()
	 * @return
	 */
	public Collection<Chess> findFallPoint() {
		return forecast();
	}

	/*计算落子位置算法*/
	/**
	 * 预获得下一步棋子的可落子位置
	 * @param color-当前棋子颜色
	 * @return
	 */
	public Collection<Chess> forecast(){
		//每颗棋子都要查找八个方向上的棋子落子点
		loopChesss();
		if(canDropSize==0) {
			//如果可落子数量为0表示对方落子，并且搜索对方是否可落子，如果对方也不能落子
			changeCuColor();
			loopChesss();
			if(canDropSize==0) {
				isOver=true;//游戏结束，双方都不能落子
			}
		}
		return canDropChessList.values();
	}
	
	private int loopChesss() {
		canDropChessList.clear();//清空上一次的可落子位置
		int dropSize=0;//可落子位置总数
		for(Chess[] row:chesss) {
			for(Chess man:row) {
				if(man==null) {
					continue;
				}
				if(man.getColor()==cuColor) {//过滤到当前颜色的棋子
					//每个棋子确定八个方向上的可落子位置
					/**算法说明：当前颜色棋子和可落子位置之间必须有连续的对方棋子,也就是说遇到己方棋子和空位则结束*/
					dropSize+=findDropChessPosition(canDropChessList,man);
					//暂时没有做到棋子去重，每个棋子可落子位置有重复
				}
			}
		}
		log.info("可落子数量："+dropSize);
		canDropSize=canDropChessList.size();
		return canDropSize;
	}
	
	/**
	 * 查找棋子所在八个方向可以落子的位置
	 * <ul><h2>查找单个棋子八个方向上的落子位置</h2>
	 * <li>x表示横轴变化趋势，y表示纵轴变化趋势，-为减少，+为增加，无符号不变</li>
	 * <li>正北(x,y-)，东北(x+,y-)，正东(x+,y)，东南(x+,y+)，正南(x,y+)，西南(x-,y+)，正西(x-,y)，西北(x-,y-)</li>
	 * <h2>单个方向上的计算逻辑</h2>
	 * <li>(一个方向上下一个位置是否有棋子--有-->是否异色棋子--是异色棋子-->继续查找下一个位置-->到达边界则结束循环)循环体</li>
	 * <li>=====================--无-->上一步是否有棋子--是-->确定当前位置为可落子位置</li>
	 * <li>=====================-----============--否-->结束循环，当前方向无落子位置</li>
	 * </ul>
	 * @return
	 */
	private int findDropChessPosition(Map<String,Chess> hasChessList,Chess chess){
		Chess line;
		int size=0;
		if((line=findDropChess0(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess1(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess2(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess3(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess4(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess5(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess6(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		if((line=findDropChess7(chess))!=null) {
			size++;
			hasChessList.put((line.getX()+","+line.getY()),line);
		}
		return size;
	}
	
	private Chess notHasAndCreateMarker(int has,int x,int y) {
		//保证当前位置无棋子
		if(hasChess(x,y)) {
			has=0;
		}
		//判断越界
		if(x<0||x>=maxSize||y<0||y>=maxSize) {
			has=0;
		}
		//如果没有棋子了，判断是否有上一个棋子
		if(has>0) {
			return createMarker(x,y);
		}
		return null;
	}
	
	/**
	 * 正北方向查找落子点
	 * @return
	 */
	private Chess findDropChess0(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(--y;hasChess(x,y);y--) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			} 
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 东北方向查找落子点
	 * @return
	 */
	private Chess findDropChess1(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(++x,--y;hasChess(x,y);x++,y--) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 正东方向查找落子点
	 * @return
	 */
	private Chess findDropChess2(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(++x;hasChess(x,y);x++) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 东南方向查找落子点
	 * @return
	 */
	private Chess findDropChess3(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(++x,++y;hasChess(x,y);x++,y++) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 正南方向查找落子点
	 * @return
	 */
	private Chess findDropChess4(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(++y;hasChess(x,y);y++) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			if(hasChess(x,y)) {
				has=0;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 西南方向查找落子点
	 * @return
	 */
	private Chess findDropChess5(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(--x,++y;hasChess(x,y);x--,y++) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		if(hasChess(x,y)) {
			has=0;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 正西方向查找落子点
	 * @return
	 */
	private Chess findDropChess6(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(--x;hasChess(x,y);x--) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	/**
	 * 西北方向查找落子点
	 * @return
	 */
	private Chess findDropChess7(Chess chess) {
		int x=chess.getX(),y=chess.getY();
		ChessColor color=chess.getColor();
		int has=0;//可以翻转的棋子
		for(--x,--y;hasChess(x,y);x--,y--) {//下一个位置上是否有棋子
			Chess c=getByArray(x,y);
			if(c.getColor()==color) {//如果遇到同色棋则跳出循环
				has=0;
				break;
			}
			//如果事异色棋子
			has++;
		}
		return notHasAndCreateMarker(has,x,y);
	}
	
	//创建可落子位置标注
	private Chess createMarker(int x,int y) {
		return new Chess(ChessColor.marker,x,y,chessWidth,chessHeight);
	}
	
	/*计算翻转棋子算法*/
	/**
	 * 计算需要翻转的棋子
	 * 
	 * @param chess
	 *            -当前落子
	 * @param xnum
	 *            -对应
	 * @param ynum
	 * @return 如果返回null，则没有需要翻转的棋子，如果不为空，则翻转为相反的棋子
	 */
	public List<Chess> reversing(int xnum,int ynum) {
		log.info("当前落子位置序号：" + xnum + "," + ynum);
		// 先检测当前位置是否被占用，如果没有，返回空
		if (!hasChess(xnum, ynum)) {
			int num = findAroundHasChess(xnum, ynum);
			if (num > 0) {// 检测周围四条直线，八个方向有无对方棋子
				lastChess=new Chess(cuColor,xnum,ynum,chessWidth,chessHeight);
				List<Chess> list = findLineChess(lastChess);
				return list;
			} else {
				return null;
			}
		} 
		//没有可落子位置
		return null;
	}
	

	/**
	 * 按照直线查找棋子
	 * 
	 * @return
	 */
	private List<Chess> findLineChess(Chess chess) {
		reverseChess.clear();
		if (aroundChess[0] != null) {// 北
			List<Chess> list = findChess0(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[1] != null) {// 东北
			List<Chess> list = findChess1(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[2] != null) {// 东
			List<Chess> list = findChess2(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[3] != null) {// 东南
			List<Chess> list = findChess3(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[4] != null) {// 南
			List<Chess> list = findChess4(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[5] != null) {// 西南
			List<Chess> list = findChess5(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[6] != null) {// 西
			List<Chess> list = findChess6(chess);
			addChessInReverseList(list);
		}
		if (aroundChess[7] != null) {// 西北
			List<Chess> list = findChess7(chess);
			addChessInReverseList(list);
		}
		log.info("需要翻转的棋子："+reverseChess);
		int size=reverseChess.size();
		if (size < 1) {
			return null;
		}
		//计算黑白棋数量
		temp=chess.getColor()==ChessColor.white?(currentWhiteChessNum+=size):(currentBlackChessNum+=size);
		temp=chess.getColor()==ChessColor.black?(currentWhiteChessNum-=size):(currentBlackChessNum-=size);
		changeHands(chess.getX(),chess.getY());//换手
		addChess(lastChess);
		return reverseChess;
	}

	/**
	 * 添加需要翻转的棋子到待翻转列表
	 * 
	 * @param list
	 * @return
	 */
	private boolean addChessInReverseList(List<Chess> list) {
		if (list != null && list.size() > 0) {
			return reverseChess.addAll(list);
		}
		return false;
	}

	/**
	 * 寻找棋子是否不满足要求(不包含边界检测)
	 * 
	 * @param x
	 * @param y
	 * @param color
	 * @return -1：
	 */
	private int findBreak(int x, int y, ChessColor color) {
		if(x<0||x>=maxSize||y<0||y>=maxSize) {
			return 2;
		}
		Chess temp = chesss[x][y];
		if (temp == null) {// 中间有空格，不满足翻转需求
			lineReverseChess.clear();
			return 2;
		}
		if (temp.getColor() == color) {// 找到同色棋子，结束寻找
			return 1;
		}else {//找到异色棋子，添加到需要翻转颜色的棋子列表
			lineReverseChess.add(temp);
			return 0;
		}
	}

	/**
	 * 寻找正北方向棋子
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess0(Chess chess) {
		lineReverseChess.clear();
//		// x轴不变，y轴减少，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX(),y=chess.getY()-1;
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; y >= 0; y--) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 寻找正南方向直线
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess4(Chess chess) {
		lineReverseChess.clear();
//		// x轴不变，y轴减少，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX(),y=chess.getY()+1;
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; y <= maxSize; y++) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 寻找正东方向棋子
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess6(Chess chess) {
		lineReverseChess.clear();
	// x轴不变，y轴减少，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()-1,y=chess.getY();
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; x >= 0; x--) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 寻找正西方向直线
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess2(Chess chess) {
		lineReverseChess.clear();
//		// x轴不变，y轴减少，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()+1,y=chess.getY();
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; x <= maxSize; x++) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 东北
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess1(Chess chess) {
		lineReverseChess.clear();
//		// x轴增加，y轴减少，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()+1,y=chess.getY()-1;
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; x <= maxSize && y >= 0; x++, y--) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 西南
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess5(Chess chess) {
		lineReverseChess.clear();
//		// x轴减少，y轴增加，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()-1,y=chess.getY()+1;
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; y <= maxSize && x >= 0; x--, y++) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 东南
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess3(Chess chess) {
		lineReverseChess.clear();
//		// x轴减少，y轴增加，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()+1,y=chess.getY()+1;
		ChessColor color = chess.getColor();
		int hasEqColor = -1;
		for (; y <= maxSize && x <= maxSize; x++, y++) {
			if ((hasEqColor = findBreak(x, y, color)) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}

	/**
	 * 西北
	 * 
	 * @param chess
	 * @return
	 */
	private List<Chess> findChess7(Chess chess) {
		lineReverseChess.clear();
//		// x轴减少，y轴增加，如果遇到空方格，则无效，如果遇到同色棋结束
		int x=chess.getX()-1,y=chess.getY()-1;
		int hasEqColor = -1;
		for (; y >= 0 && x >= 0; x--, y--) {
			if ((hasEqColor = findBreak(x, y, chess.getColor())) > 0) {
				break;
			}
		}
		if (hasEqColor != 1) {
			return null;
		}
		return lineReverseChess;
	}
	
	/**
	 * 游戏是否结束
	 * @return
	 */
	public boolean isOver() {
		//棋盘上已经放满
		if(isOver||currentChessNum==maxSize*maxSize) {
			return true;
		}
		return false;
	}

	/**
	 * 重新开始游戏
	 */
	public void restart() {
		start();
	}
}
