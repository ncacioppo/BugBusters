package bugbusters.UI;

import bugbusters.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static bugbusters.UI.Globals.actualUser;
import static bugbusters.UI.Globals.userSchedules;
import static bugbusters.UI.Navigation.*;

public class CompareSchedulesController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private TabPane tbLeft;
    @FXML
    private TabPane tbRight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbLeft.getTabs().add(tab);
        }

        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbRight.getTabs().add(tab);
        }
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
