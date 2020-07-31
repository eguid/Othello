package cc.eguid.game.othello.scene;

import java.util.logging.Logger;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * 模式选择界面
 * @author eguid
 *
 */
public class ChooseModeScene extends ViewTemplate implements BaseWindow{

	final Logger log=Logger.getLogger("对战模式选择界面");
	
	@Override
	public void load() {
		initWindow("奥赛罗(黑白棋) -eguid作品", 400, 400);
		Font font=new Font(30);
		Text numText=new Text(100,80," 对战模式选择 ");
		numText.setFont(font);
		
		Button manBattle=new Button("双人对战");
		manBattle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			
			// 棋子画板鼠标点击处理
			@Override
			public void handle(MouseEvent event) {
				log.info("切换到双人对战界面");
				
				//切换到游戏：双人对战界面
				switchWindow(new ManScene());
			}
		});
		Button machineBattle=new Button("双机对战");
		machineBattle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			
			// 棋子画板鼠标点击处理
			@Override
			public void handle(MouseEvent event) {
				log.info("切换到双电脑模拟对战界面");
				
				//切换到游戏：双人对战界面
				switchWindow(new MachineBattleScene());//切换窗口，顺便关闭当前窗口
			}

		});
		Button manMachineBattle=new Button("人机对战");
		manMachineBattle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			
			// 棋子画板鼠标点击处理
			@Override
			public void handle(MouseEvent event) {
				log.info("切换到人机对战界面");
				
				//切换到游戏：双人对战界面
				switchWindow(new ManMachineBattleScene());
			}
		});
		Button netBattle=new Button("网络对战");
		netBattle.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			
			// 棋子画板鼠标点击处理
			@Override
			public void handle(MouseEvent event) {
				log.info("切换到网络对战界面");
				
				//切换到游戏：双人对战界面
				switchWindow(new NetBattleScene());
			}
		});
		Text copyrightText=new Text(110,260," Copyright©2018-2020 ");
		Text authorText=new Text(130,300," 制作人：eguid ");
		Text author1Text=new Text(110,330," 策划：eguid ");
		Text author2Text=new Text(190,330," 程序：eguid ");
		Text author3Text=new Text(110,350," 美术：eguid ");
		Text author4Text=new Text(190,350," 音效：eguid ");
		copyrightText.setFont(new Font(14));
		authorText.setFont(new Font(16));
		author1Text.setFont(new Font(12));
		author2Text.setFont(new Font(12));
		author3Text.setFont(new Font(12));
		author4Text.setFont(new Font(12));
		HBox mb=null;//控制面板
		//播放器控制面板
		
		//水平放置容器
		mb = new HBox();
		//布局面板容器
		Pane pane=new StackPane(mb);
		pane.setLayoutX(50);
		pane.setLayoutY(150);
		
		mb.setAlignment(Pos.CENTER);//设置对齐方式：中间对齐
		mb.setPadding(new Insets(5, 10, 5, 10));//设置内边距
		StackPane.setAlignment(mb, Pos.CENTER);//设置对其方式：中间对齐
		Label spacer = new Label(" ");
		Label spacer2 = new Label(" ");
		Label spacer3= new Label(" ");
		mb.getChildren().addAll(manBattle,spacer,machineBattle, spacer2,manMachineBattle,spacer3,netBattle);
		
		addNode(numText,pane,copyrightText,authorText,author1Text,author2Text,author3Text,author4Text);
	}
	
	@Override
	protected Scene initScene() {
		return new Scene(new Group(),400,400);
	}

}
