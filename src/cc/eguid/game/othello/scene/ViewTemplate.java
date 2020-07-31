package cc.eguid.game.othello.scene;

import java.util.Map;
import java.util.Optional;

import cc.eguid.game.othello.LaunchUtil;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * 界面通用模板类
 * 
 * @author eguid
 *
 */
public abstract class ViewTemplate extends Application implements BaseWindow {
	private Stage stage;// 窗体
	private Scene scene;// 场景
	private Group root;// 场景下的根节点，用于添加节点
	private String title;// 窗口标题
	private int width, height;// 窗口界面的宽高

	public void initWindow(String title, int width, int height) {
		this.title = title;// 窗口标题
		this.width = width;// 窗口界面的宽高
		this.height = height;
	}

	protected abstract Scene initScene();

	@Override
	public void start(Stage primaryStage) throws Exception {
		scene = initScene();
		root = (Group) scene.getRoot();
		load();// 加载一些东西
		stage = primaryStage;
		stage.setTitle(getTitle());
		stage.setWidth(getWidth());
		stage.setHeight(getHeight());
		stage.setScene(scene);
		show();// 显示界面
	}
	
	public void setScene(Scene scene) {
		this.scene=scene;
		stage.setScene(scene);
	}
	
	public void hideScene() {
		stage.setScene(null);
	}
	
	public void showScene() {
		stage.setScene(scene);
	}

	/**
	 * 添加节点
	 * 
	 * @param node
	 */
	public void addNode(Node... nodes) {
		root.getChildren().addAll(nodes);
	}
	
	/**
	 * 删除节点
	 * @param node
	 */
	public void removeNode(Node node) {
		root.getChildren().remove(node);
	}
	
	/**
	 * 批量删除节点
	 * @param nodes
	 */
	public void removeAllNode(Node ...nodes) {
		root.getChildren().removeAll(nodes);
	}

	@Override
	public void close() {
		stage.close();
	}

	@Override
	public void hide() {
		stage.hide();
	}

	@Override
	public void show() {
		stage.show();
	}

	@Override
	public void switchFullscreen() {
		stage.setFullScreen(!stage.isFullScreen());
	}

	/**
	 * 获取窗体
	 * 
	 * @return
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * 获取场景
	 * 
	 * @return
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * 获取窗体标题
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 获取窗体标题
	 * 
	 * @return
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取窗体宽度
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * 设置窗体宽度
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * 获取窗体高度
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * 设置窗体高度
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * 获取全部参数
	 * 
	 * @return
	 */
	public Map<String, String> getParams() {
		return LaunchUtil.getPrams(this);
	}

	/**
	 * 获取参数
	 * 
	 * @param key
	 * @return
	 */
	public String getParam(String key) {
		return LaunchUtil.getParam(this, key);
	}

	/**
	 * 切换窗口
	 */
	public void switchWindow(String className) {
		this.close();
		try {
			LaunchUtil.launch(className);
		} catch (ClassNotFoundException e) {
		}
	}

	/**
	 * 切换窗口，并顺便关闭当前窗口
	 */
	public void switchWindow(Application app) {
		switchWindow(app,true);
	}
	
	/**
	 * 切换窗口
	 * @param app 待切换的窗口
	 * @param closeThis 是否顺便关闭当前窗口
	 */
	public void switchWindow(Application app,boolean closeThis) {
		if(closeThis) {
			this.close();
		}
		try {
			app.start(new Stage());
		} catch (Exception e) {
		}
	}

	/**
	 * 弹出一个通用的确定对话框
	 * 
	 * @param p_header  对话框的信息标题
	 * @param p_message 对话框的信息
	 * @return 用户点击了是或否
	 */
	public boolean confirmDialog(String p_header, String p_message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, p_message, new ButtonType("取消", ButtonBar.ButtonData.NO),
				new ButtonType("确定", ButtonBar.ButtonData.YES));
		alert.setTitle(p_header);
		alert.setHeaderText(p_header);
		alert.initOwner(stage);
		Optional<ButtonType> _buttonType = alert.showAndWait();// showAndWait() 将在对话框消失以前不会执行之后的代码
		return (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES));
	}

	/**
	 * 弹出一个信息对话框
	 * 
	 * @param p_header  对话框的信息标题
	 * @param p_message 对话框的信息
	 */
	public void infoDialog(String p_header, String p_message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(p_header);
		alert.setHeaderText(p_header);
		alert.setContentText(p_message);
		alert.initOwner(stage);
		alert.show();
	}

}
