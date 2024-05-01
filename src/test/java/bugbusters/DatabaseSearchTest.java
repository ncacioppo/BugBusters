package bugbusters;

import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseSearchTest {
    //TODO: Handle queries like "comp sci spring 2020" as
    // select CourseID, CourseName,Dept from course where CourseName LIKE '%comp%' and coursename like '%sci%' and semester = 'spring' and year = 2020;

    @Test
    public void DatabaseSearchConstructorTest() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        assertEquals("SELECT * FROM course", search.getQuery().toString());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByDepartment() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.DEPARTMENT, "HUMA");

        ArrayList<Course> results = search.getResults();
        assertEquals(230, results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByID() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.ID, "294");
        ArrayList<Course> results = search.getResults();

        assertEquals(1, results.size());
        assertEquals(294, results.get(0).getId());
//        Run.printCourses(results);
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByName() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.NAME, "ELECTRIC");  //name contains

        ArrayList<Course> results = search.getResults();
        assertEquals(12,results.size());
//        Run.printCourses(results);
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByNameAndDept() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.NAME, "ELECTRIC");  //name contains
        search.applyFilter(Filter.DEPARTMENT, "PHYS");

        ArrayList<Course> results = search.getResults();
        assertEquals(3,results.size());
//        Run.printCourses(results);
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByCodeMin() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.CODE_MIN, "300");

        ArrayList<Course> results = search.getResults();
        assertEquals(1756,results.size());
        registrar.disconnectFromDB();
    }
    @Test
    public void FilterByCodeBetween() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.CODE_MIN, "200");
        search.applyFilter(Filter.CODE_MAX,"300");

        ArrayList<Course> results = search.getResults();
        assertEquals(1431,results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByTermSpring2018() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.TERM, "Spring 2018");
        ArrayList<Course> results = search.getResults();
        assertEquals(765,results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByTwoTerms() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.TERM, "Spring 2018");
        search.applyFilter(Filter.TERM, "Fall 2018");
        ArrayList<Course> results = search.getResults();
        assertEquals(0,results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByProfessor() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.TERM, "Spring 2020");
        search.applyFilter(Filter.PROFESSOR, "trueman");

        ArrayList<Course> results = search.getResults();
        assertEquals(3,results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByDay() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.DAY, "TR");
        search.applyFilter(Filter.DEPARTMENT, "PSYC");

        ArrayList<Course> results = search.getResults();
        assertEquals(34,results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void FilterByTimeMax() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.TIME_MAX, "9:00:00");

        ArrayList<Course> results = search.getResults();
        assertEquals(1391,results.size());
        registrar.disconnectFromDB();
    }
    @Test
    public void FilterByTimeBetween() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.TIME_MIN, "11:00:00");
        search.applyFilter(Filter.DEPARTMENT,"CHEM");
        search.applyFilter(Filter.TIME_MAX,"14:00:00");

        ArrayList<Course> results = search.getResults();
        assertEquals(21,results.size());
        registrar.disconnectFromDB();
    }



    @Test
    public void RemoveDeptFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.TERM,"Fall 2019");
        search.applyFilter(Filter.DEPARTMENT,"CHEM");
        search.removeFilter(Filter.DEPARTMENT, "CHEM");

        ArrayList<Course> results = search.getResults();
        assertEquals(756,results.size());
        registrar.disconnectFromDB();
    }


    @Test
    public void RemoveTimeFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.TIME_MIN, "11:00:00");
        search.applyFilter(Filter.DEPARTMENT,"CHEM");
        search.applyFilter(Filter.TIME_MAX,"14:00:00");
        search.removeFilter(Filter.TIME_MIN, "11:00:00");

        ArrayList<Course> results = search.getResults();
        assertEquals(153, results.size());
        registrar.disconnectFromDB();
    }



    @Test
    public void RemoveCodeFilter() {
        ArrayList<Course> results;
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.CODE_MAX, "200");
        search.applyFilter(Filter.DEPARTMENT,"SOCW");
        search.removeFilter(Filter.CODE_MAX,"200");
        search.applyFilter(Filter.CODE_MAX, "300");

        results = search.getResults();
        assertEquals(19, results.size());
        registrar.disconnectFromDB();
    }



    @Test
    public void RemoveTermFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.applyFilter(Filter.CODE_MAX, "300");
        search.applyFilter(Filter.TERM,"Fall 2018");
        search.removeFilter(Filter.TERM, "faLL 2018");

        ArrayList<Course> results = search.getResults();
        assertEquals(2777, results.size());
        registrar.disconnectFromDB();
    }



    @Test
    public void RemoveDayFilter() {
        ArrayList<Course> results;
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.applyFilter(Filter.DAY, "MWF");
        search.removeFilter(Filter.DAY, "MWF");
        search.applyFilter(Filter.DAY, "TR");

        results = search.getResults();
        assertEquals(1127, results.size());
        registrar.disconnectFromDB();
    }


    @Test
    public void keywordSearchNameAndDept() throws FileNotFoundException {
        ArrayList<Course> results;
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.keywordSearch("leecrtical negienerign");
        results = search.getResults();

        search.applyFilter(Filter.DAY,"MWF");
        results = search.getResults();

        assertEquals(3, results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void changeSearchTerm() throws FileNotFoundException {
        ArrayList<Course> results;
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.keywordSearch("electrical engineering");
        results = search.getResults();

        search.applyFilter(Filter.PROFESSOR,"Powell");
        results = search.getResults();

        search.keywordSearch("principles of marketing");
        results = search.getResults();

        assertEquals(10, results.size());
        registrar.disconnectFromDB();
    }


    @Test
    public void RemoveAllFilters() throws FileNotFoundException {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.keywordSearch("electrical engineering");
        search.applyFilter(Filter.DAY,"MWF");
        search.removeFilter(Filter.DAY, "MWF");
        search.keywordSearch("");

        ArrayList<Course> results = search.getResults();
        assertEquals(4526, results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void RemoveAllFiltersAndSearch() throws FileNotFoundException {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.keywordSearch("statistics");
        search.applyFilter(Filter.CODE_MIN,"200");
        search.removeFilter(Filter.CODE_MIN, "200");

        ArrayList<Course> results = search.getResults();
        assertEquals(26, results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void SearchDeptAndCode() throws FileNotFoundException {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.keywordSearch("statistics");
        search.applyFilter(Filter.CODE_MIN,"200");
        search.removeFilter(Filter.CODE_MIN, "200");

        search.keywordSearch("aoidsj math 331");
        ArrayList<Course> results = search.getResults();
        assertEquals(3, results.size());
        registrar.disconnectFromDB();
    }

    @Test
    public void RemoveNonexistentFilter() throws FileNotFoundException {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.keywordSearch("statistics");
        search.removeFilter(Filter.CODE_MIN, "200");

        ArrayList<Course> results = search.getResults();
        assertNotEquals(0, results.size());
        registrar.disconnectFromDB();
    }
    @Test
    public void MiscSearchTesting() throws FileNotFoundException {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.keywordSearch("a;oijdf ojefoaj i028 20 332");

        ArrayList<Course> results = search.getResults();
        assertEquals(0, results.size());
        registrar.disconnectFromDB();
    }
}