package bugbusters.UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static bugbusters.UI.Navigation.toSearchCalendar;
import static bugbusters.UI.Navigation.toUserSchedules;

public class CompareSchedulesController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    @FXML
    public void toSchedules(MouseEvent event) throws IOException {
        toUserSchedules(mainPane);
    }
    @FXML
    public void toSearch(MouseEvent event) throws IOException {
        toSearchCalendar(mainPane);
    }

}
