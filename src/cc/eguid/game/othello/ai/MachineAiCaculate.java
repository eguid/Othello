package cc.eguid.game.othello.ai;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import cc.eguid.game.othello.ctrl.OthelloAlgorithm;
import cc.eguid.game.othello.obj.Chess;

/**
 * 基于固定行为匹配模式的人工智障
 * 
 * 前置条件：
 * 1、获取落子位置
 * 2、落子位置大于1
 * 算法设计：
 * 1、尽可能的占据四角(稳定子，不可被翻转)
 * 2、优先占据中间四行四列格子（尽可能的占据中间位置，后面会按照哪个位置可以翻转更多棋子优先更高方式优化）
 * 3、上述不行，选择占据贴边位置（不包含四角临近的三个，尽可能的不占据四角位置）
 * 4、其他位置实在没办法尽可能的翻转更多棋子
 * 
 * 落子黑名单：
 * 1、四角临近的三个
 * 2、尽可能不陷入贴边的双空位陷阱
 * @author eguid
 *
 */
public class MachineAiCaculate implements AiCaculate{

	final Logger log=Logger.getLogger("ai算法");
	
	OthelloAlgorithm othelloAlgorithm;
	
	public MachineAiCaculate(OthelloAlgorithm othelloAlgorithm) {
		this.othelloAlgorithm=othelloAlgorithm;
	}
	
	@Override
	public Chess caculate() {
		int size=0;
		//获取当前可落子数量
		if((size=othelloAlgorithm.getCanDropSize())>0) {
			//获取当前可落子棋子位置
			Collection<Chess> droplist = othelloAlgorithm.getCanDropChessList();
			
			//不可落子
			if(droplist==null||droplist.size()<1) {
				return null;
			}
			
			//只有一个落子位置,不用算了，直接返回这个落子位置
			if(size==1) {
				return onlyOneDropPostionHandle(droplist);
			}
			
			List<Chess> canDropChess=new ArrayList<>(4);
			int canDropSize=0;
			//算法：
			//四角位置可落子，优先级最高
			if((canDropSize=searchCornersChessPostion(droplist,canDropChess))>0) {
				log.warning("人工智障：找到【角落】数量："+canDropChess.size());
				if(canDropSize==1) {
					log.warning("人工智障找到一个【角落】");
					return onlyOneDropPostionHandle(canDropChess);
				}
				log.warning("人工智障找到多个【角落】");
				return randomChessPostionHandle(canDropChess,canDropSize);
			}
			//中间四格优先级次之
			if((canDropSize=searchCenterChessPostion(droplist,canDropChess))>0) {
				
				log.warning("人工智障：找到【中间】四格数量："+canDropChess.size());
				if(canDropSize==1) {
					log.warning("人工智障找到【中间】四格");
					return onlyOneDropPostionHandle(canDropChess);
				}
				log.warning("人工智障找到【中间】四格");
				return randomChessPostionHandle(canDropChess,canDropSize);	
			}
			
			//边棋再次之（边棋要避免双空陷阱和破坏自己的双空陷阱）
			if((canDropSize=searchSideChessPostion(droplist,canDropChess))>0) {
				
				log.warning("人工智障：找到【四边】数量："+canDropChess.size());
				if(canDropSize==1) {
					log.warning("人工智障找到【四边】四格");
					return onlyOneDropPostionHandle(canDropChess);
				}
				log.warning("人工智障找到【四边】四格");
				return randomChessPostionHandle(canDropChess,canDropSize);	
			}
			//如果没有匹配到上述规则，则获取一个随机可落子位置返回
			return randomChessPostionHandle(droplist,size);
			
		}
		return null;
	}
	
	/**
	 * 只有一个棋子
	 * @return 
	 */
	private Chess onlyOneDropPostionHandle(Collection<Chess> droplist) {
		Object[] elementData = droplist.toArray();
		return (Chess) elementData[0];
	}
	
	/**
	 * 搜索四边可落子棋子
	 * @param droplist
	 * @param canDropChess
	 * @return
	 */
	private int searchSideChessPostion(Collection<Chess> droplist, List<Chess> canDropChess) {
		int num=0;
		int maxSize=othelloAlgorithm.getMaxSize()-1;
		//搜索可落子棋子中是四个角位置的数量
		for(Chess chess:droplist) {
			if(isSideChess(chess,maxSize)) {
				canDropChess.add(chess);
				num++;
			}
			if(num>3) {
				break;
			}
		}
		return num;
	}
	

	/**
	 * 搜索四角可落子位置
	 * @param droplist
	 * @return
	 */
	private int searchCornersChessPostion(Collection<Chess> droplist,List<Chess> canDropChess) {
		int num=0;
		int maxSize=othelloAlgorithm.getMaxSize()-1;
		//搜索可落子棋子中是四个角位置的数量
		for(Chess chess:droplist) {
			if(isCornersChess(chess,maxSize)) {
				canDropChess.add(chess);
				num++;
			}
			if(num>3) {
				break;
			}
		}
		return num;
	}
	
	/**
	 * 搜索中间四格可落子位置
	 * @param droplist 当前可落子位置
	 * @param canDropChess 存放落子位置
	 * @return
	 */
	private int searchCenterChessPostion(Collection<Chess> droplist,List<Chess> canDropChess) {
		int num=0;
		int maxSize=othelloAlgorithm.getMaxSize()-1;
		//搜索可落子棋子中是四个角位置的数量
		for(Chess chess:droplist) {
			if(isCenterChess(chess,maxSize)) {
				canDropChess.add(chess);
				num++;
			}
			if(num>12) {
				break;
			}
		}
		return num;
	}
	
	/**
	 * 棋子位置是否是四角位置
	 * @param chess
	 * @param maxSize
	 * @return
	 */
	private boolean isCornersChess(Chess chess,int maxSize) {
		int x=chess.getX();
		int y=chess.getY();
		if(x==0&&y==0) {
			return true;
		}else if(x==0&&y==maxSize) {
			return true;
		}else if(x==maxSize&&y==0) {
			return true;
		}else if(x==maxSize&&y==maxSize) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否中间四格棋子
	 * @param chess
	 * @param maxSize
	 * @return
	 */
	private boolean isCenterChess(Chess chess,int maxSize) {
		int x=chess.getX();
		int y=chess.getY();
		
		if(x>1&&y>1&&x<(maxSize-2)&&y<(maxSize-2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 是否四边位置
	 * @param chess
	 * @param maxSize
	 * @return
	 */
	private boolean isSideChess(Chess chess, int maxSize) {
		int x=chess.getX();
		int y=chess.getY();
		if(x==0&&y>1&&y<(maxSize-2)) {
			return true;
		}else if(y==0&&x>1&&x<(maxSize-2)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获得一个可落子位置的随机位置
	 * @param droplist
	 * @param size
	 * @return
	 */
	private Chess randomChessPostionHandle(Collection<Chess> droplist,int size) {
		int ranNum=(int) (size*Math.random());
		for(Chess chess:droplist) {
			ranNum--;
			if(ranNum==0) {
				return chess;
			}
		}
		return null;
	}
}
