package bugbusters.UI;

import bugbusters.*;
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
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import bugbusters.UI.Navigation.*;
import org.apache.commons.lang3.tuple.Pair;

import static bugbusters.UI.Globals.*;
import static bugbusters.UI.Navigation.*;

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

            Schedule newSchedule = new Schedule(actualUser, shcedName, new Term(schedSemester, schedYear), new ArrayList<>(), new ArrayList<>());

            userSchedules.put(newSchedule.getName(), newSchedule);
            Tab tab = new Tab(newSchedule.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = new GridPane();
            Text schedText = new Text();
            schedText.setText(newSchedule.toString());
            gridPane.getChildren().add(schedText);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSchedules.getTabs().add(tab);

        } else {}
    }

    @FXML
    public void handleUndo(MouseEvent event){
        currentSchedule.undoChange();
        actualUser.getRegistrar().saveSchedule(currentSchedule);

        userSchedules = new HashMap<>();
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

    }

    @FXML
    public void handleRedo(MouseEvent event){

        currentSchedule.redoChange();
        actualUser.getRegistrar().saveSchedule(currentSchedule);

        userSchedules = new HashMap<>();
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

    }

    @FXML
    void handleAvatarClick(MouseEvent event) throws IOException {

        GridPane grid = new GridPane();

        Label name = new Label(actualUser.getFirstName() + " " + actualUser.getLastName());
        grid.addRow(0);
        grid.addRow(1, name);

        Label majors = new Label("Majors: ");
        grid.addRow(2, majors);

        int count = 2;
        for (Major major : actualUser.getUserMajors()){
            count+=1;
            grid.addRow(count, new Label("\t" + major.toString()));
        }

        count+=1;
        grid.addRow(count, new Label("Minors: "));

        for (Minor minor : actualUser.getUserMinors()){
            count+=1;
            grid.addRow(count, new Label("\t" + minor.toString()));
        }

        count+=1;
        grid.addRow(count, new Label("College year: " + actualUser.getCollegeYear()));

        ButtonType cancelButton = new ButtonType("Done", ButtonBar.ButtonData.CANCEL_CLOSE);
        //ButtonType confirmButton = new ButtonType("Import my info from MyGCC", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.NONE);

        alert.setTitle("User Info");
        alert.setHeaderText("View your info or import it from MyGCC");
        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().setAll(cancelButton/**, confirmButton**/);
        Optional<ButtonType> result = alert.showAndWait();

//        if (result.isPresent() && result.get() == confirmButton) {
//            // Process input if Confirm button is clicked
//            ButtonType cancelButton2 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//            ButtonType confirmButton2 = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
//
//            Alert alert2 = new Alert(Alert.AlertType.NONE);
//
//            alert2.setTitle("Do you want to log in to MyGCC");
//            alert2.setHeaderText("If you log into MyGCC you will need your authenticator app");
//            alert2.getButtonTypes().setAll(cancelButton2, confirmButton2);
//            Optional<ButtonType> result2 = alert2.showAndWait();
//            if (result2.isPresent() && result2.get() == confirmButton2) {
//                GridPane grid3 = new GridPane();
//                grid3.addRow(0);
//                TextField username = new TextField();
//                username.setPromptText("Username");
//                grid3.addRow(1, username);
//                PasswordField password = new PasswordField();
//                grid3.addRow(2, password);
//
//                ButtonType cancelButton3 = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
//                ButtonType confirmButton3 = new ButtonType("Import my info from MyGCC", ButtonBar.ButtonData.OK_DONE);
//
//                Alert alert3 = new Alert(Alert.AlertType.NONE);
//
//                alert3.setTitle("Log into MyGCC");
//                alert3.setHeaderText("Enter your information to log into MyGCC");
//                alert3.getDialogPane().setContent(grid3);
//                alert3.getButtonTypes().setAll(cancelButton3, confirmButton3);
//                Optional<ButtonType> result3 = alert3.showAndWait();
//
//                if (result3.isPresent() && result3.get() == confirmButton3) {
//                    actualUser.importInfo(username.getText(), password.getText());
//
//                    GridPane grid4 = new GridPane();
//                    grid4.addRow(0, new Label("Please wait for your authentication code"));
//
//                    while (actualUser.myGCC.authenticationNum.equalsIgnoreCase("0")){}
//                    grid4.addRow(1, new Label("Here is the authentication code for your authenticator app:"));
//                    grid4.addRow(2, new Label(actualUser.myGCC.authenticationNum));
//
//
//                    ButtonType cancelButton4 = new ButtonType("Done", ButtonBar.ButtonData.CANCEL_CLOSE);
//
//                    Alert alert4 = new Alert(Alert.AlertType.NONE);
//
//                    alert4.setTitle("Here is your authentication code");
//                    alert4.setHeaderText("This may take a minute or two... Please be patient.");
//                    alert4.getDialogPane().setContent(grid4);
//                    alert4.getButtonTypes().setAll(cancelButton4);
//                    Optional<ButtonType> result4 = alert4.showAndWait();
//
//
//
//                }
//            }
//        }
    }

}
