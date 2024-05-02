package bugbusters.UI;

import bugbusters.Course;
import bugbusters.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static bugbusters.UI.Navigation.*;

public class SearchCalendarController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ImageView addButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

    }

    @FXML void toSchedules(MouseEvent event) throws IOException {
        toUserSchedules(mainPane);
    }

    @FXML
    public void toCompare(MouseEvent event) throws IOException {
        toCompareSchedules(mainPane);
    }

    @FXML
    public void toOtherSearch(MouseEvent event) throws IOException {
        toSearchList(mainPane);
    }

    @FXML
    public void toConflict(MouseEvent event) throws IOException {
        handleConflict(mainPane);
    }
}
