package cc.eguid.game.othello.ai;

import java.util.Collection;
import java.util.logging.Logger;

import cc.eguid.game.othello.ctrl.OthelloAlgorithm;
import cc.eguid.game.othello.obj.Chess;

/**
 * 基于随机算法的人工智障下棋ai。 最简单的ai设计，随机落子， 界面提供当前落子情况和可落子位置给ai， ai经过随机计算得到一个位置，并返回落子位置，
 * 界面将ai返回的落子位置提供给算法并绘制到棋盘。
 * 
 * @author eguid
 *
 */
public class RandomMachineAiCaculate implements AiCaculate {

	final Logger log = Logger.getLogger("ai算法");

	OthelloAlgorithm othelloAlgorithm;

	public RandomMachineAiCaculate(OthelloAlgorithm othelloAlgorithm) {
		this.othelloAlgorithm = othelloAlgorithm;
	}

	/**
	 * 计算返回落子
	 * 
	 * @return 棋子落子坐标
	 */
	public Chess caculate() {
		int size = 0;
		// 可落子位置
		if ((size = othelloAlgorithm.getCanDropSize()) > 0) {
			Collection<Chess> droplist = othelloAlgorithm.getCanDropChessList();
			if (droplist != null && droplist.size() > 0) {
				Object[] elementData = droplist.toArray();
				if (size == 1) {
					return (Chess) elementData[0];
				}

				int ranNum = (int) (size * Math.random());

				for (Chess chess : droplist) {
					ranNum--;
					if (ranNum == 0) {
						return chess;
					}
				}
			}
		}
		return null;
	}

}
