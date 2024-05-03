package bugbusters.UI;

//Red Hex Color: #d00404

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static bugbusters.UI.Navigation.*;

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
                            save = save + timeStamp.getYear() + "-" + timeStamp.getMonthValue() + "-" + timeStamp.getDayOfMonth() + "T" + timeStamp.getHour() + ":" + timeStamp.getMinute() + ":" + timeStamp.getSecond() + " : " + line.split(" : ")[1].split(" - ")[0] + " - " + line.split(" : ")[1].split(" - ")[1] + "\n";
                        }
                    }
                }

                byte[] bytes = save.getBytes();
                Files.write(path, bytes);

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
        lstUsers.setVisible(chkRemember.isSelected());
    }

    @FXML
    void updateUsername(KeyEvent event){
        username = txtUsername.getText() + event.getText();
    }

    @FXML
    void updatePassword(KeyEvent event){
        password = txtPassword.getText() + event.getText();
        txtPasswordShow.setText(password);
    }

    @FXML
    void updatePasswordShown(KeyEvent event){
        password = txtPasswordShow.getText() + event.getText();
        txtPassword.setText(password);
    }

    @FXML
    void toSchedules(MouseEvent event) throws IOException {
        toUserSchedules(mainPane);
    }

}
