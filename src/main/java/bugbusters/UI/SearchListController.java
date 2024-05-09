package bugbusters.UI;

import bugbusters.*;
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
import javafx.scene.text.Text;

import javax.swing.text.html.ImageView;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static bugbusters.UI.Globals.*;
import static bugbusters.UI.Navigation.*;

public class SearchListController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ListView<Course> lstCourses;
    @FXML
    private Text txtCourseInfo;
    @FXML
    private TextArea txtDescription;
    @FXML
    private TextField minCode;
    @FXML
    private TextField maxCode;
    @FXML
    private ChoiceBox<String> deptFilter;
    @FXML
    private ChoiceBox<String> termFilter;
    @FXML
    private ChoiceBox<String> professorFilter;
    @FXML
    private TextField txtKeyWord;
    @FXML
    private CheckBox MWFfilter;
    @FXML
    private CheckBox TRfilter;
    @FXML
    private TabPane tbSched;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        searchCourses = dbSearch.getResults();
        ObservableList<Course> items = FXCollections.observableArrayList(searchCourses);
        lstCourses.setItems(items);

        if (currentCourse.getId() != -999){
            lstCourses.getSelectionModel().select(currentCourse);
            lstCourses.getFocusModel().focus(lstCourses.getSelectionModel().getSelectedIndex());
            txtCourseInfo.setText("Course info:\n" + Globals.currentCourse.toLongString());
            txtDescription.setText("Description: \n" + Globals.currentCourse.getDescription());
        }

        txtKeyWord.setText(currentKeyword);
        deptFilter.getSelectionModel().select(currentDept);
        termFilter.getSelectionModel().select(currentTerm);
        professorFilter.getSelectionModel().select(currentProfessor);
        MWFfilter.setSelected(currentMWF);
        TRfilter.setSelected(currentTR);
        minCode.setText(String.valueOf(currentMinCode));
        maxCode.setText(String.valueOf(currentMaxCode));

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
            tbSched.getTabs().add(tab);
        }

        selectTab(tbSched, currentSchedule.getName());

        ArrayList<Integer> codes = new ArrayList<>();
        for (int i=1; i<1000; i++){
            codes.add(i);
        }

        for (Course course : searchCourses){
            departments.add(course.getDepartment());
            terms.add(course.getTerm().toString());
            professors.add(course.getInstructor());
        }
        ArrayList<String> temp = new ArrayList<>(professors);
        Collections.sort(temp);

        ObservableList<String> departmentList = FXCollections.observableArrayList(departments);
        deptFilter.getItems().add("");
        deptFilter.getItems().addAll(departmentList);
        deptFilter.setValue("");

        ObservableList<String> seasonList = FXCollections.observableArrayList(terms);
        termFilter.getItems().add("");
        termFilter.getItems().addAll(seasonList);
        termFilter.setValue("");

        ObservableList<String> professorList = FXCollections.observableArrayList(temp);
        professorFilter.getItems().add("");
        professorFilter.getItems().addAll(professorList);

        deptFilter.setOnAction(event -> {
            if (!deptFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.DEPARTMENT, prevDept);
                prevDept = deptFilter.getValue().toString();
                dbSearch.applyFilter(Filter.DEPARTMENT, deptFilter.getValue().toString());
                currentDept = deptFilter.getValue().toString();
            } else {
                dbSearch.removeFilter(Filter.DEPARTMENT, prevDept);
                prevDept = deptFilter.getValue().toString();
                currentDept = "";
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
            updateChoices();
        });

        termFilter.setOnAction(event-> {
            if (!termFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.TERM, prevTerm);
                prevTerm = termFilter.getValue().toString();
                dbSearch.applyFilter(Filter.TERM, termFilter.getValue().toString());
                currentTerm = termFilter.getValue().toString();
            } else {
                dbSearch.removeFilter(Filter.TERM, prevTerm);
                prevTerm = termFilter.getValue().toString();
                currentTerm = "";
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
            updateChoices();
        });

        professorFilter.setOnAction(event-> {
            if (!professorFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.PROFESSOR, prevProf);
                prevProf = professorFilter.getValue().toString();
                dbSearch.applyFilter(Filter.PROFESSOR, professorFilter.getValue().toString());
                currentProfessor = professorFilter.getValue().toString();
            } else {
                dbSearch.removeFilter(Filter.PROFESSOR, prevProf);
                prevProf = professorFilter.getValue().toString();
                currentProfessor = "";
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
            updateChoices();
        });

    }

    @FXML
    public void handleMinCode(KeyEvent event){
        String min = minCode.getText() + event.getText();
        currentMinCode = Integer.parseInt(min);
        dbSearch.removeFilter(Filter.CODE_MIN, prevMin);
        prevMin = min;
        dbSearch.applyFilter(Filter.CODE_MIN, min);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
        updateChoices();
    }

    @FXML
    public void handleMaxCode(KeyEvent event){
        String max = maxCode.getText() + event.getText();
        currentMaxCode = Integer.parseInt(max);
        dbSearch.removeFilter(Filter.CODE_MAX, prevMax);
        prevMax = max;
        dbSearch.applyFilter(Filter.CODE_MAX, max);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
        updateChoices();
    }

    @FXML void handleKeyword(KeyEvent event){

        String keyWord = txtKeyWord.getText() + event.getText();
        currentKeyword = keyWord;

//        dbSearch.removeFilter(Filter.KEYWORD, prevKeyword);
//        prevKeyword = keyWord;
        dbSearch.keywordSearch(keyWord);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
        updateChoices();
    }

    @FXML
    public void handleListViewClick(MouseEvent event){
        Globals.currentCourse = lstCourses.getSelectionModel().getSelectedItem();
        txtCourseInfo.setText("Course info:\n" + Globals.currentCourse.toLongString());
        txtDescription.setText("Description: \n" + Globals.currentCourse.getDescription());
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

    @FXML
    public void toConflict(MouseEvent event) throws IOException {
        try {
            handleConflict(mainPane);
        } catch (NullPointerException e){
            ButtonType cancelButton = new ButtonType("OK", ButtonBar.ButtonData.CANCEL_CLOSE);
            ButtonType confirmButton = new ButtonType("Show me potential alternatives", ButtonBar.ButtonData.OK_DONE);

            Alert alert = new Alert(Alert.AlertType.NONE);

            alert.setTitle("Course Conflict");
            alert.setHeaderText("The course you tried to add conflicts with another course in your " + currentSchedule.getName() + "schedule");
            alert.getButtonTypes().setAll(cancelButton, confirmButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == confirmButton) {
                currentKeyword = currentCourse.getDepartment() + " " + currentCourse.getCode() + " " + currentCourse.getSection();
                currentTerm = "";
                currentProfessor = "";
                currentDept = "";
                currentMinCode = 0;
                currentMaxCode = 699;
                currentMWF = false;
                currentTR = false;
                txtKeyWord.setText(currentKeyword);
                deptFilter.getSelectionModel().select(currentDept);
                termFilter.getSelectionModel().select(currentTerm);
                professorFilter.getSelectionModel().select(currentProfessor);
                MWFfilter.setSelected(currentMWF);
                TRfilter.setSelected(currentTR);
                minCode.setText(String.valueOf(currentMinCode));
                maxCode.setText(String.valueOf(currentMaxCode));

                dbSearch = new DatabaseSearch(actualUser.getRegistrar().getConn(), actualUser);
                dbSearch.keywordSearch(currentKeyword);

                ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
                lstCourses.setItems(searchResults);
            }
        }

        TabPane temp = new TabPane();

        userSchedules = new HashMap<>();
        tbSched.getTabs().clear();
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
            tbSched.getTabs().add(tab);
        }
    }

    @FXML
    public void handleMWF(ActionEvent event){
        if (MWFfilter.isSelected()){
            currentMWF = true;
            dbSearch.applyFilter(Filter.DAY, "MWF");
        } else {
            currentMWF = false;
            dbSearch.removeFilter(Filter.DAY, "MWF");
        }
        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
        updateChoices();
    }

    @FXML
    public void handleTR(ActionEvent event){
        if (TRfilter.isSelected()){
            currentTR = true;
            dbSearch.applyFilter(Filter.DAY, "TR");
        } else {
            currentTR = false;
            dbSearch.removeFilter(Filter.DAY, "TR");
        }
        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
        updateChoices();
    }

    @FXML
    public void handleRemove(MouseEvent event){
        GridPane grid = new GridPane();
        grid.addRow(0, new Label("Please select the course you would like to delete from your " + currentSchedule.getName() + " schedule"));
        ChoiceBox courseOptions = new ChoiceBox();

        ObservableList<Course> coursesInSchedule = FXCollections.observableArrayList(currentSchedule.getCourses());
        courseOptions.getItems().add("");
        courseOptions.getItems().addAll(coursesInSchedule);
        grid.addRow(1, courseOptions);

        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        ButtonType confirmButton = new ButtonType("Delete Course", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.NONE);

        alert.setTitle("Delete course");
        alert.setHeaderText("Please select the course you would like to delete from your " + currentSchedule.getName() + " schedule");
        alert.getDialogPane().setContent(grid);
        alert.getButtonTypes().setAll(cancelButton, confirmButton);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == confirmButton) {
            try {
                currentSchedule.removeCourse((Course) courseOptions.getSelectionModel().getSelectedItem());
                actualUser.getRegistrar().saveSchedule(currentSchedule);
            } catch (Exception e){}
        }

        tbSched.getTabs().clear();
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
            tbSched.getTabs().add(tab);
        }
    }

    @FXML
    public void updateChoices(){

//        departments = new HashSet<>();
//        terms = new HashSet<>();
//        professors = new HashSet<>();
//
//        for (Course course : dbSearch.getResults()){
//            departments.add(course.getDepartment());
//            terms.add(course.getTerm().toString());
//            professors.add(course.getInstructor());
//        }
//        ArrayList<String> temp = new ArrayList<>(professors);
//        Collections.sort(temp);
//
//        String deptSelected = deptFilter.getValue();
//        deptFilter.getItems().clear();
//        ObservableList<String> departmentList = FXCollections.observableArrayList(departments);
//        deptFilter.getItems().add("");
//        deptFilter.getItems().addAll(departmentList);
//        deptFilter.setValue(deptSelected);
//
//        ObservableList<String> seasonList = FXCollections.observableArrayList(terms);
//        termFilter.getItems().add("");
//        termFilter.getItems().addAll(seasonList);
//
//        ObservableList<String> professorList = FXCollections.observableArrayList(temp);
//        professorFilter.getItems().add("");
//        professorFilter.getItems().addAll(professorList);
    }

    @FXML
    public void handleUndo(MouseEvent event){
        currentSchedule.undoChange();
        actualUser.getRegistrar().saveSchedule(currentSchedule);

        userSchedules = new HashMap<>();
        tbSched.getTabs().clear();
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
            tbSched.getTabs().add(tab);
        }

    }

    @FXML
    public void handleRedo(MouseEvent event){

        currentSchedule.redoChange();
        actualUser.getRegistrar().saveSchedule(currentSchedule);

        userSchedules = new HashMap<>();
        tbSched.getTabs().clear();
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
            tbSched.getTabs().add(tab);
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
