package bugbusters.UI;

//Red Hex Color: #d00404

import bugbusters.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static bugbusters.UI.Navigation.*;
import static bugbusters.UI.Globals.*;

public class LoginController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtPasswordShow;
    @FXML
    private CheckBox chkRemember;
    @FXML
    private CheckBox chkShowPassword;
    @FXML
    private ListView lstUsers;

    private String username;
    private String password;
    private Map<String, Pair<String, LocalDateTime>> userInfo = new HashMap<>();

    private int listViewItemHeight = 25;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){

        File cookieFile = new File("userCookie.txt");
        Path path = Paths.get("userCookie.txt");
        String save = "";
        lstUsers.setEditable(false);


        if (cookieFile.exists()){
            //File Exists
            try {
                // Read all lines from the file
                List<String> lines = Files.readAllLines(path);

                for (String line : lines){
                    String strDateTime = line.split(" : ")[0];
                    if (strDateTime.split("T").length == 2){
                        String[] strDate = strDateTime.split("T")[0].split("-");
                        String[] strTime = strDateTime.split("T")[1].split(":");
                        LocalDateTime timeStamp = LocalDateTime.of(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]), Integer.parseInt(strDate[2]), Integer.parseInt(strTime[0]), Integer.parseInt(strTime[1]), Integer.parseInt(strTime[2]));
                        if (Duration.between(timeStamp, LocalDateTime.now()).toSeconds() < 864000){
                            userInfo.put(line.split(" : ")[1].split(" - ")[0], Pair.of(line.split(" : ")[1].split(" - ")[1], timeStamp));
                        }
                    }
                }

                if (userInfo.keySet().size() > 0){
                    chkRemember.setSelected(true);
                }
            } catch (IOException e) {}
        } else {
            try{
                cookieFile.createNewFile();
            } catch (IOException e){}
        }

        username = "";
        password = "";
        if (chkRemember.isSelected()){
            ObservableList<String> items = FXCollections.observableArrayList(userInfo.keySet());
            lstUsers.setItems(items);
        } else {

        }
    }

    @FXML
    void showListView (MouseEvent event){
        if (chkRemember.isSelected()) {
            lstUsers.setPrefHeight(listViewItemHeight*Math.min(userInfo.keySet().size(), 10));
            lstUsers.setMinHeight(listViewItemHeight*Math.min(userInfo.keySet().size(), 10));
            lstUsers.setMaxHeight(listViewItemHeight*Math.min(userInfo.keySet().size(), 10));
            lstUsers.setVisible(true);
        }
    }

    @FXML
    void handleListViewClick(MouseEvent event){
        username = lstUsers.getSelectionModel().getSelectedItem().toString();
        password = userInfo.get(username).getLeft();
        txtUsername.setText(username);
        txtPassword.setText(password);
        txtPasswordShow.setText(password);
        lstUsers.setVisible(false);
    }

    @FXML
    void switchPasswordView(ActionEvent event){
        txtPasswordShow.setVisible(!txtPasswordShow.isVisible());
        txtPassword.setVisible(!txtPasswordShow.isVisible());
    }

    @FXML
    void switchRememberMe(ActionEvent event){
        if (chkRemember.isSelected() && userInfo.keySet().size() > 0){
            lstUsers.setVisible(true);
        } else {
            lstUsers.setVisible(false);
        }
    }

    @FXML
    void updateUsername(KeyEvent event){
        username = txtUsername.getText() + event.getText();
        lstUsers.setVisible(false);
    }

    @FXML
    void updatePassword(KeyEvent event){
        password = txtPassword.getText() + event.getText();
        txtPasswordShow.setText(password);
        lstUsers.setVisible(false);
    }

    @FXML
    void updatePasswordShown(KeyEvent event){
        password = txtPasswordShow.getText() + event.getText();
        txtPassword.setText(password);
        lstUsers.setVisible(false);
    }

    @FXML
    void handleLoginClick(ActionEvent event) throws IOException {
        username = txtUsername.getText();
        password = txtPassword.getText();
        User tempUser = new User(username, password);
        if (tempUser.getUserID() == -999){
            //User doesn't exists on database
            Button ok = new Button("OK");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Username and password incorrect");
            alert.setHeaderText("Incorrect username and password");
            alert.setContentText("The username and password you entered do not exist. \n" +
                    "Try signing up or looking for any errors in your log in information.");
            alert.show();


        } else {
            //User does exist
            actualUser = tempUser;

            if (chkRemember.isSelected()){
                if (userInfo.get(username) == null){
                    userInfo.put(username, Pair.of(password, LocalDateTime.now()));
                }
            } else {
                userInfo.remove(username);
            }
            String save = "";

            for (String line : userInfo.keySet()){
                LocalDateTime timeStamp = userInfo.get(line).getRight();
                save = save + timeStamp.getYear() + "-" + timeStamp.getMonthValue() + "-" + timeStamp.getDayOfMonth() + "T" + timeStamp.getHour() + ":" + timeStamp.getMinute() + ":" + timeStamp.getSecond() + " : " + line + " - " + userInfo.get(line).getLeft() + "\n";
            }

            byte[] bytes = save.getBytes();
            Files.write(Paths.get("userCookie.txt"), bytes);

            toUserSchedules(mainPane);
        }
    }

    @FXML
    void handleSignUpClick(ActionEvent event) throws IOException {
        username = txtUsername.getText();
        password = txtPassword.getText();
        User tempUser = new User(username, password);
        if (tempUser.getUserID() == -999){
            //User doesn't exist on database
            TextField firstName = new TextField();
            firstName.setPromptText("First Name");
            TextField lastName = new TextField();
            lastName.setPromptText("Last Name");
            Spinner<Integer> year = new Spinner();
            year.setPromptText("Graduation Year");
            int now = LocalDate.now().getYear();
            SpinnerValueFactory<Integer> yearValueFactory =
                    new SpinnerValueFactory.IntegerSpinnerValueFactory(now, now + 10, now);
            year.setValueFactory(yearValueFactory);
            year.setEditable(true);
            Spinner<String> month = new Spinner();
            month.setPromptText("Graduation Month");
            String[] months = {"Jan", "Feb", "March", "Apr", "May", "June", "July", "Aug", "Sep", "Oct", "Nov", "Dec"};
            SpinnerValueFactory<String> monthValueFactory =
                    new SpinnerValueFactory.ListSpinnerValueFactory<>(FXCollections.observableArrayList(months));
            month.setValueFactory(monthValueFactory);
            month.setEditable(true);
            ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType confirmButton = new ButtonType("Sign Up", ButtonBar.ButtonData.OK_DONE);
            GridPane grid = new GridPane();

            grid.addRow(0);
            grid.addRow(1, firstName);
            grid.addRow(2, lastName);
            grid.addRow(3, year);
            grid.addRow(4, month);
            grid.addRow(5);

            Alert alert = new Alert(Alert.AlertType.NONE);

            alert.setTitle("Signing up");
            alert.setHeaderText("Enter your information in below to sign up");
            alert.getDialogPane().setContent(grid);
            alert.getButtonTypes().setAll(cancelButton, confirmButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == confirmButton) {
                // Process input if Confirm button is clicked
                String fName = firstName.getText();
                String lName = lastName.getText();
                int gradYear = year.getValue();
                String gradMonth = month.getValue();

                actualUser = new User(username, password, fName, lName, gradYear, gradMonth);

                if (chkRemember.isSelected()){
                    if (userInfo.get(username) == null){
                        userInfo.put(username, Pair.of(password, LocalDateTime.now()));
                    }
                } else {
                    userInfo.remove(username);
                }
                String save = "";

                for (String line : userInfo.keySet()){
                    LocalDateTime timeStamp = userInfo.get(line).getRight();
                    save = save + timeStamp.getYear() + "-" + timeStamp.getMonthValue() + "-" + timeStamp.getDayOfMonth() + "T" + timeStamp.getHour() + ":" + timeStamp.getMinute() + ":" + timeStamp.getSecond() + " : " + line + " - " + userInfo.get(line).getLeft() + "\n";
                }

                byte[] bytes = save.getBytes();
                Files.write(Paths.get("userCookie.txt"), bytes);

                toUserSchedules(mainPane);

            } else {}
        } else {
            //User does exist
            Button ok = new Button("OK");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Username and password already exists");
            alert.setHeaderText("Username and password already exists");
            alert.setContentText("The username and password you entered already exists. \n" +
                    "Try logging in instead.");
            alert.show();
        }
    }

}
