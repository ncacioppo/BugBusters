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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;

import static bugbusters.UI.Globals.*;
import static bugbusters.UI.Navigation.*;

public class SearchCalendarController implements Initializable {

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
    private ChoiceBox deptFilter;
    @FXML
    private ChoiceBox termFilter;
    @FXML
    private ChoiceBox professorFilter;
    @FXML
    private TextField txtKeyWord;
    @FXML
    CheckBox MWFfilter;
    @FXML
    CheckBox TRfilter;
    @FXML
    TabPane tbSched;

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

        ObservableList<String> seasonList = FXCollections.observableArrayList(terms);
        termFilter.getItems().add("");
        termFilter.getItems().addAll(seasonList);

        ObservableList<String> professorList = FXCollections.observableArrayList(temp);
        professorFilter.getItems().add("");
        professorFilter.getItems().addAll(professorList);

        userSchedules = new HashMap<>();
        System.out.println("size: " + userSchedules.keySet().size());
        System.out.println(actualUser.getSchedules().size());
        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            System.out.println("size: " + userSchedules.keySet().size());
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSched.getTabs().add(tab);
        }

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
        });

        tbSched.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            if (newTab != null) {
                currentSchedule = userSchedules.get(newTab.getText());
            }
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
    }

    @FXML void handleKeyword(KeyEvent event){

        String keyWord = txtKeyWord.getText() + event.getText();
        currentKeyword = keyWord;

//        dbSearch.removeFilter(Filter.KEYWORD, prevKeyword);
//        prevKeyword = keyWord;
        dbSearch.keywordSearch(keyWord);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
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
        toSearchList(mainPane);
    }

    @FXML
    public void toConflict(MouseEvent event) throws IOException {
        handleConflict(mainPane);

        TabPane temp = new TabPane();

        userSchedules = new HashMap<>();
        tbSched.getTabs().clear();
        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
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
    }

    @FXML
    public void handleRemove(MouseEvent event){
        currentSchedule.removeCourse(currentCourse);

        tbSched.getTabs().clear();
        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSched.getTabs().add(tab);
        }
    }

    @FXML
    public void handleUndo(MouseEvent event){
        currentSchedule.undoChange();
        actualUser.getRegistrar().saveSchedule(currentSchedule);

        userSchedules = new HashMap<>();
        tbSched.getTabs().clear();
        for (Schedule sched : actualUser.getSchedules()){
            userSchedules.put(sched.getName(), sched);
            System.out.println("size: " + userSchedules.keySet().size());
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
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
            System.out.println("size: " + userSchedules.keySet().size());
            Tab tab = new Tab(sched.getName());
            ScrollPane scrlPane = new ScrollPane();
            GridPane gridPane = createCalendarView(sched);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSched.getTabs().add(tab);
        }

    }


}
