package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.SceneNavigator;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Locale locale = new Locale("al");
            ResourceBundle bundle = ResourceBundle.getBundle("bundles.Messages", locale, getClass().getClassLoader());
            URL fxmlLocation = getClass().getResource("/views/login.fxml");
            if (fxmlLocation == null) {
                System.out.println("❌ login.fxml nuk u gjet! Kontrollo rrugën.");
                return;
            } else {
                System.out.println("✅ login.fxml u gjet me sukses: " + fxmlLocation);
            }

            FXMLLoader loader = new FXMLLoader(fxmlLocation, bundle);

            Parent root = loader.load();
            primaryStage.setTitle(bundle.getString("loginTitle"));
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            SceneNavigator.initializeHistory("/views/login.fxml");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
