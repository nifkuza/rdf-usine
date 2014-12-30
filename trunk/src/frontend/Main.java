package frontend;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.stage.Stage;

/**
 * 
 * @author Maximiliano Ariel Lopez (maxilopez@gmail.com)
 *
 */
public class Main extends Application 
{
	public static Stage primaryStage;

	@Override
	public void start(Stage primaryStage) throws IOException 
	{
		this.primaryStage = primaryStage;

		ResourceBundle bundle = ResourceBundle.getBundle("bundles.bundle", Locale.forLanguageTag("en"));
		BorderPane center = FXMLLoader.load(getClass().getResource("App.fxml"), bundle);
		BorderPane bp = BorderPaneBuilder.create().center(center).build();
		
		primaryStage.setScene(new Scene(bp, 1280, 800));
		primaryStage.setTitle(bundle.getString("application"));
		primaryStage.show();
	}

	/**
	 * The main() method is ignored in correctly deployed JavaFX application.
	 * main() serves only as fallback in case the application can not be
	 * launched through deployment artifacts, e.g., in IDEs with limited FX
	 * support. NetBeans ignores main().
	 *
	 * @param args the command line arguments
	 */
	public static void main(String[] args) 
	{
		launch(args);
	}
}