package bugbusters.UI;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static bugbusters.UI.Navigation.*;

public class SearchListController implements Initializable {

    @FXML
    private BorderPane mainPane;

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
        toSearchCalendar(mainPane);
    }


}
