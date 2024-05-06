package bugbusters.UI;

import bugbusters.Schedule;
import bugbusters.Term;
import bugbusters.User;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import bugbusters.UI.Navigation.*;
import org.apache.commons.lang3.tuple.Pair;

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

        if (userSchedules.keySet().size() >0) {
            currentSchedule = userSchedules.get((new ArrayList<>(userSchedules.keySet())).get(0));
        }

        if (currentSchedule != null) {
            scrlCalendar.setContent(createCalendarView(currentSchedule));
        }

    }

    @FXML
    public void toCompare(MouseEvent event) throws IOException {
        toCompareSchedules(mainPane);
    }
    @FXML
    public void toSearch(MouseEvent event) throws IOException {
        toSearchCalendar(mainPane);
    }

    @FXML
    public void handleAddSchedule(MouseEvent event){
//User doesn't exist on database
        TextField name = new TextField();
        name.setPromptText("Schedule Name");

        Spinner<Integer> year = new Spinner();
        year.setPromptText("Year");
        int now = LocalDate.now().getYear();
        SpinnerValueFactory<Integer> yearValueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(now, now + 10, now);
        year.setValueFactory(yearValueFactory);
        year.setEditable(true);
        Spinner<String> semester = new Spinner();
        semester.setPromptText("Semester");
        String[] semesters = {"Fall", "Spring"};
        SpinnerValueFactory<String> monthValueFactory =
                new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(semesters));
        semester.setValueFactory(monthValueFactory);
        semester.setEditable(true);
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
        GridPane grid = new GridPane();

        grid.addRow(0);
        grid.addRow(1, name);
        grid.addRow(3, year);
        grid.addRow(4, semester);
        grid.addRow(5);

        Alert alert = new Alert(Alert.AlertType.NONE);

        alert.setTitle("Create a schedule");
        alert.setHeaderText("Enter your schedule information below to create a schedule");
        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().setAll(cancelButton, confirmButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton) {
            // Process input if Confirm button is clicked
            String shcedName = name.getText();
            int schedYear = year.getValue();
            String schedSemester = semester.getValue();

            actualUser.addSchedule(new Schedule(actualUser, shcedName, new Term(schedSemester, schedYear), new ArrayList<>(), new ArrayList<>()));

            tbSchedules.getTabs().clear();
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


        } else {}
    }

}
