package cc.eguid.game.othello.scene;

/**
 * 基础窗口
 * 
 * @author eguid
 *
 */
public interface BaseWindow {

	/**
	 * 关闭窗口
	 */
	public void close();

	/**
	 * 隐藏窗口
	 */
	public void hide();
	
	/**
	 * 显示
	 */
	public void show();
	
	/**
	 * 全屏切换
	 */
	public void switchFullscreen();
	
	/**
	 * 用于加载一些东西
	 */
	public void load();

}
