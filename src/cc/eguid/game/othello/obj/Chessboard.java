package cc.eguid.game.othello.obj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 棋盘
 * @author eguid
 *
 */
public class Chessboard implements BaseCanvasDraw{

	int cellSize;//格子数量（长宽相等，9或16）
	int cellWidth;//每个格子宽
	int cellHeight;//每个格子高
	
	int pixelWidth;//棋盘总像素宽度
	int pixelHeight;//棋盘总像素高度
	
	Color lineColor = Color.BLACK;// 线条颜色
	int lineThick = 1;// 线条粗度
	Color backgroud =Color.GREEN;// 背景颜色
	
	public Chessboard(int cellSize, int cellWidth, int cellHeight, int pixelWidth, int pixelHeight, Color lineColor,
			int lineThick, Color backgroud) {
		super();
		this.cellSize = cellSize;
		this.cellWidth = cellWidth;
		this.cellHeight = cellHeight;
		this.pixelWidth = pixelWidth;
		this.pixelHeight = pixelHeight;
		this.lineColor = lineColor;
		this.lineThick = lineThick;
		this.backgroud = backgroud;
	}
	
	public int getCellSize() {
		return cellSize;
	}

	public void setCellSize(int cellSize) {
		this.cellSize = cellSize;
	}

	public int getCellWidth() {
		return cellWidth;
	}

	public void setCellWidth(int cellWidth) {
		this.cellWidth = cellWidth;
	}

	public int getCellHeight() {
		return cellHeight;
	}

	public void setCellHeight(int cellHeight) {
		this.cellHeight = cellHeight;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(int pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(int pixelHeight) {
		this.pixelHeight = pixelHeight;
	}

	public Color getLineColor() {
		return lineColor;
	}

	public void setLineColor(Color lineColor) {
		this.lineColor = lineColor;
	}

	public int getLineThick() {
		return lineThick;
	}

	public void setLineThick(int lineThick) {
		this.lineThick = lineThick;
	}

	public Color getBackgroud() {
		return backgroud;
	}

	public void setBackgroud(Color backgroud) {
		this.backgroud = backgroud;
	}

	@Override
	public String toString() {
		return "Chessboard [cellSize=" + cellSize + ", cellWidth=" + cellWidth + ", cellHeight=" + cellHeight
				+ ", pixelWidth=" + pixelWidth + ", pixelHeight=" + pixelHeight + ", lineColor=" + lineColor
				+ ", lineThick=" + lineThick + ", backgroud=" + backgroud + "]";
	}

	@Override
	public void draw(GraphicsContext gc) {
		gc.setLineWidth(getLineThick());
		//填充棋盘颜色
		gc.setFill(getBackgroud());
		int axisNum = getCellSize();
		gc.fillRect(0, 0, getPixelWidth(), getPixelHeight());
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
	 * 格子坐标转换为像素坐标
	 * 
	 * @param x
	 *            -格子坐标
	 * @param y
	 *            -格子坐标
	 * @return
	 */
	public int[] getXY(int x, int y) {
		int[] xy = {x * getCellWidth(), y * getCellHeight() };
		return xy;
	}
	
	/**
	 * 是否在棋盘边界范围之内
	 * 
	 * @return
	 */
	public boolean inBounds(int x, int y) {
		int sumW=getPixelWidth();
		int sumH=getPixelHeight();
		if (x<=0 ||x >= sumW || y<= 0|| y >= sumH) {
			return false;
		}
		return true;
	}
}