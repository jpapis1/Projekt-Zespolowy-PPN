package app.view.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class LoginWindow extends Application {
    public static void run() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window/login.fxml"));
        loader.setControllerFactory(app.Application.app::getBean);
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root, 800, 400);

        stage.setTitle("Trade and Stocks");
        stage.setScene(scene);
        stage.show();
    }
}
