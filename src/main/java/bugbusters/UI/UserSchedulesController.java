package bugbusters.UI;

import bugbusters.Schedule;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import bugbusters.UI.Navigation.*;

import static bugbusters.UI.Globals.*;
import static bugbusters.UI.Navigation.*;
//import static bugbusters.UI.Navigation.toSearchCalendar;

public class    UserSchedulesController implements Initializable {

    @FXML
    private ImageView compareSchedules;
    @FXML
    private ImageView searchSchedules;
    @FXML
    private BorderPane mainPane;
    @FXML
    private TabPane tbSchedules;
    @FXML
    private ScrollPane scrlCalendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = new GridPane();
            Text schedText = new Text();
            schedText.setText(sched.toString());
            gridPane.getChildren().add(schedText);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSchedules.getTabs().add(tab);
        }

        tbSchedules.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                currentSchedule = userSchedules.get(newTab.getText());
                scrlCalendar.setContent(createCalendarView(currentSchedule));
            }
        });

        currentSchedule = userSchedules.get((new ArrayList<>(userSchedules.keySet())).get(0));

        scrlCalendar.setContent(createCalendarView(currentSchedule));

    }

    @FXML
    public void toCompare(MouseEvent event) throws IOException {
        toCompareSchedules(mainPane);
    }
    @FXML void toSearch(MouseEvent event) throws IOException {
        toSearchCalendar(mainPane);
    }

}
