package app.view.window;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;

public class LoginWindow extends Application {
    static public ApplicationContext appContext;

    public static void run(ApplicationContext context) {
        appContext = context;
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println(appContext);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/window/login.fxml"));
        loader.setControllerFactory(appContext::getBean);
        Parent root = loader.load();

        //Parent root = FXMLLoader.load(getClass().getResource("/fxml/login.fxml"));
        Scene scene = new Scene(root, 1000, 750);

        stage.setTitle("Trade and Stocks");
        stage.setScene(scene);
        stage.show();
    }
}
