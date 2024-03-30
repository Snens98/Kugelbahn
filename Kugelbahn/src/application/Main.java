package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.security.AccessControlException;
import java.util.Objects;


public class Main extends Application {

	public static final double PixelPerMeter = 50.0;
	public static final double frametime = (1.0/60.0);

	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {

		try {
			System.setProperty("prism.verbose", "true");
			System.setProperty("prism.dirtyopts", "false");
			System.setProperty("javafx.animation.fullspeed", "true");
			System.setProperty("javafx.animation.pulse", "10");
		} catch (AccessControlException e) {}

		try {		
			GridPane root2 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainWindow.fxml")));
			Scene scene2 = new Scene(root2, 1100, 650);
			primaryStage.setScene(scene2);
			primaryStage.show();
	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}
