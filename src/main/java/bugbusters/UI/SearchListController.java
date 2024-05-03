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
import java.util.ResourceBundle;

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
    private ChoiceBox deptFilter;
    @FXML
    private ChoiceBox termFilter;
    @FXML
    private ChoiceBox professorFilter;
    @FXML
    private TextField txtKeyWord;
    @FXML
    private CheckBox MWFfilter;
    @FXML
    private CheckBox TRfilter;
    @FXML
    private TabPane tbSched;

    private DatabaseSearch dbSearch;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        dbSearch = new DatabaseSearch(actualUser.getRegistrar().getConn(), actualUser);
        ObservableList<Course> items = FXCollections.observableArrayList(searchCourses);
        lstCourses.setItems(items);

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

        deptFilter.setOnAction(event -> {
            if (!deptFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.DEPARTMENT, prevDept);
                prevDept = deptFilter.getValue().toString();
                dbSearch.applyFilter(Filter.DEPARTMENT, deptFilter.getValue().toString());
            } else {
                dbSearch.removeFilter(Filter.DEPARTMENT, prevDept);
                prevDept = deptFilter.getValue().toString();
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
        });

        termFilter.setOnAction(event-> {
            if (!termFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.TERM, prevTerm);
                prevTerm = termFilter.getValue().toString();
                dbSearch.applyFilter(Filter.TERM, termFilter.getValue().toString());
            } else {
                dbSearch.removeFilter(Filter.TERM, prevTerm);
                prevTerm = termFilter.getValue().toString();
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
        });

        professorFilter.setOnAction(event-> {
            if (!professorFilter.getValue().toString().equalsIgnoreCase("")) {
                dbSearch.removeFilter(Filter.PROFESSOR, prevProf);
                prevProf = professorFilter.getValue().toString();
                dbSearch.applyFilter(Filter.PROFESSOR, professorFilter.getValue().toString());
            } else {
                dbSearch.removeFilter(Filter.PROFESSOR, prevProf);
                prevProf = professorFilter.getValue().toString();
            }
            ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
            lstCourses.setItems(searchResults);
        });

    }

    @FXML
    public void handleMinCode(KeyEvent event){
        String min = minCode.getText() + event.getText();
        dbSearch.removeFilter(Filter.CODE_MIN, prevMin);
        prevMin = min;
        dbSearch.applyFilter(Filter.CODE_MIN, min);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
    }

    @FXML
    public void handleMaxCode(KeyEvent event){
        String max = maxCode.getText() + event.getText();
        dbSearch.removeFilter(Filter.CODE_MAX, prevMax);
        prevMax = max;
        dbSearch.applyFilter(Filter.CODE_MAX, max);

        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
    }

    @FXML void handleKeyword(KeyEvent event){

        String keyWord = txtKeyWord.getText() + event.getText();

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
        toSearchCalendar(mainPane);
    }

    @FXML
    public void toConflict(MouseEvent event) throws IOException {
        handleConflict(mainPane);

        TabPane temp = new TabPane();

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
            dbSearch.applyFilter(Filter.DAY, "MWF");
        } else {
            dbSearch.removeFilter(Filter.DAY, "MWF");
        }
        ObservableList<Course> searchResults = FXCollections.observableArrayList(dbSearch.getResults());
        lstCourses.setItems(searchResults);
    }

    @FXML
    public void handleTR(ActionEvent event){
        if (TRfilter.isSelected()){
            dbSearch.applyFilter(Filter.DAY, "TR");
        } else {
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
            GridPane gridPane = new GridPane();
            Text schedText = new Text();
            schedText.setText(sched.toString());
            gridPane.getChildren().add(schedText);
            scrlPane.setContent(gridPane);
            tab.setContent(scrlPane);
            tbSched.getTabs().add(tab);
        }
    }
}
