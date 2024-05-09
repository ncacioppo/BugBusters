package bugbusters.UI;

import bugbusters.Major;
import bugbusters.Minor;
import bugbusters.Schedule;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
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
