package cc.eguid.game.othello.obj;

/**
 * 棋子类（也可仅当作坐标点使用）
 * @author eguid
 *
 */
public class Chess{

	ChessColor color;//棋子颜色
	int x;//格子坐标x
	int y;//格子坐标y
	
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
	
	public Chess(ChessColor color, int x, int y) {
		super();
		this.color = color;
		this.x = x;
		this.y = y;
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
	
}
