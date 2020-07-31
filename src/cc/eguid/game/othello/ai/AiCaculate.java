package cc.eguid.game.othello.ai;

import cc.eguid.game.othello.obj.Chess;

/**
 * 人工智障计算通用接口
 * @author eguid
 *
 */
public interface AiCaculate {
	
	/**
	 * 通过计算得到落子位置
	 * @return
	 */
	public Chess caculate();
}
