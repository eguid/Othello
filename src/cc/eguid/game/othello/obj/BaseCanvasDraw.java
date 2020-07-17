package cc.eguid.game.othello.obj;

import javafx.scene.canvas.GraphicsContext;

/**
 * canvas绘制基接口，所有需要在canvas上绘制的对象需要实现该接口
 * @author eguid
 *
 */
public interface BaseCanvasDraw {
	void draw(GraphicsContext gc);
}
