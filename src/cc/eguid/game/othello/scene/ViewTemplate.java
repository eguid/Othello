package cc.eguid.game.othello.scene;

import java.util.Optional;

import javafx.application.Application;
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
	protected Stage stage;

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

	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		stage.setTitle(getTitle());
		if (getScene() != null) {
			stage.setScene(getScene());
		}
		show();//显示界面
		load();// 加载一些东西
	}

	protected abstract Scene getScene();

	protected abstract String getTitle();

	/**
	 * 弹出一个通用的确定对话框
	 * 
	 * @param p_header
	 *            对话框的信息标题
	 * @param p_message
	 *            对话框的信息
	 * @return 用户点击了是或否
	 */
	public boolean confirmDialog(String p_header, String p_message) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, p_message, new ButtonType("取消", ButtonBar.ButtonData.NO),new ButtonType("确定", ButtonBar.ButtonData.YES));
		alert.setTitle(p_header);
		alert.setHeaderText(p_header);
		alert.initOwner(stage);
		Optional<ButtonType> _buttonType = alert.showAndWait();// showAndWait() 将在对话框消失以前不会执行之后的代码
		return (_buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES));
	}

	/**
	 * 弹出一个信息对话框
	 * 
	 * @param p_header 对话框的信息标题
	 * @param p_message  对话框的信息
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
