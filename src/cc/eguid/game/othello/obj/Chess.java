package cc.eguid.game.othello.obj;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * 棋子类（也可仅当作坐标点使用）
 * @author eguid
 *
 */
public class Chess implements BaseCanvasDraw{

	ChessColor color;//棋子颜色
	int x;//格子坐标x
	int y;//格子坐标y
	
	int width;//棋子宽度
	int height;//妻子高度
	
	public Chess() {
		super();
	}

	public Chess(ChessColor color) {
		super();
		this.color = color;
	}

	public Chess( int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public Chess(ChessColor color, int x, int y,int width,int height) {
		super();
		this.color = color;
		this.x = x;
		this.y = y;
		this.width=width;
		this.height=height;
	}
	
	public ChessColor getColor() {
		return color;
	}

	public void setColor(ChessColor color) {
		this.color = color;
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public String toString() {
		return "Chess [color=" + color + ", x=" + x + ", y=" + y + "]";
	}

	@Override
	public void draw(GraphicsContext gc) {
		draw(gc,width,height);
	}
	
	private void draw(GraphicsContext gc,Integer width,Integer height) {
		Color color=ChessColor.white == getColor() ? Color.WHITE : Color.BLACK;
		gc.setFill(color);
		gc.fillOval(getX()*width, getY()*height, width, height);
	}
}
