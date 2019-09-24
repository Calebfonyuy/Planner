package application;
	
import java.io.FileReader;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import controllers.HomeViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;


public class Planner extends Application {
	private ResourceBundle resource;
	@Override
	public void start(Stage primaryStage) {
		resource = getResource();
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomeView.fxml"),resource);
			VBox root = loader.load(); 
			primaryStage.setScene(new Scene(root));
			primaryStage.setTitle(resource.getString("window.title"));
			primaryStage.setMinHeight(600);
			primaryStage.setMinWidth(800);
			primaryStage.getIcons().add(new Image(getClass().getResource("logo.png").toString()));
			primaryStage.setOnCloseRequest((e)->{
				((HomeViewController)loader.getController()).closeConnection();
				System.exit(1);
			});
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	private static ResourceBundle getResource() {
		Properties language = new Properties();
		try {
			FileReader reader = new FileReader("Database/lang.properties");
			language.load(reader);
			return ResourceBundle.getBundle("bundles.message", new Locale(language.getProperty("locale")));
		}catch(IOException ex) {
			return ResourceBundle.getBundle("bundles.message", new Locale("en"));
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
