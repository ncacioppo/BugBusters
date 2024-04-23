package bugbusters.UI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import bugbusters.UI.Navigation.*;

import static bugbusters.UI.Navigation.toCompareSchedules;
import static bugbusters.UI.Navigation.toSearchCalendar;

public class UserSchedulesController implements Initializable {

    @FXML
    private ImageView compareSchedules;
    @FXML
    private ImageView searchSchedules;
    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    @FXML
    public void toCompare(MouseEvent event) throws IOException {
        toCompareSchedules(mainPane);
    }
    @FXML void toSearch(MouseEvent event) throws IOException {
        toSearchCalendar(mainPane);
    }

}
