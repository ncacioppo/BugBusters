package bugbusters;

import bugbusters.Scraping.MyGCC;
import bugbusters.Scraping.UpdatedCourses;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import static bugbusters.Run.run;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/bugbusters/UI/Login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("Grove City College Scheduler");
        Scene scene = new Scene(root);
//        String css = this.getClass().getResource("Applications.css").toExternalForm();
//        scene.getStylesheets().add(css);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
    public static void main(String[] args) {
//        run();
        launch();
    }

}