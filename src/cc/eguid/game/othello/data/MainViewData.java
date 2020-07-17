package cc.eguid.game.othello.data;

import cc.eguid.game.othello.obj.Chessboard;
import javafx.scene.paint.Color;

/**
 * 主页面数据
 * @author eguid
 *
 */
public class MainViewData {
	String name;//主页面标题
	
	Chessboard chessboard;//棋盘
	
	Integer width,height;

	public MainViewData() {
		super();
	}

	public MainViewData(String name,Integer width, Integer height,int cellSize,int cellWidth,int cellHeight) {
		super();
		this.name = name;
		this.chessboard =new Chessboard(cellSize, cellWidth,cellHeight,cellSize*cellWidth,cellSize*cellHeight, Color.RED, 1, Color.DARKSEAGREEN);
		this.width = width;
		this.height = height;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Chessboard getChessboard() {
		return chessboard;
	}

	public void setChessboard(Chessboard chessboard) {
		this.chessboard = chessboard;
	}
	
	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	@Override
	public String toString() {
		return "MainViewData [name=" + name + ", chessboard=" + chessboard
				+  ", width=" + width + ", height=" + height + "]";
	}

}
