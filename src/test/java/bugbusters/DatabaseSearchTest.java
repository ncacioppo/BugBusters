package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseSearchTest {
    //TODO: Handle queries like "comp sci spring 2020" as
    // select CourseID, CourseName,Dept from course where CourseName LIKE '%comp%' and coursename like '%sci%' and semester = 'spring' and year = 2020;

    //TODO: Remove filter

    @Test
    public void DatabaseSearchConstructorTest() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        assertEquals("SELECT * FROM course", search.getQuery().toString());
    }

    @Test
    public void ExecuteQuery() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.setQuery("SELECT * FROM course WHERE CourseID = 2 OR CourseID = 60");
        assertEquals(2, search.executeQuery().size());
    }

    @Test
    public void SearchByDepartment() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.DEPARTMENT, "HUMA");
        assertEquals(230,search.executeQuery().size());
    }

    @Test
    public void SearchByID() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.ID, "294");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1,results.size());
        assertEquals(294, results.get(0).getId());
        Run.printCourses(results);
    }

    @Test
    public void SearchByName() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.NAME, "ELECTRIC");  //name contains

        ArrayList<Course> results = search.executeQuery();
        assertEquals(12,results.size());
        Run.printCourses(results);
    }

    @Test
    public void SearchByNameAndDept() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.NAME, "ELECTRIC");  //name contains
        search.addFilter(Filter.DEPARTMENT, "PHYS");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(3,results.size());
        Run.printCourses(results);
    }

    @Test
    public void SearchByCodeMin() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.CODE_MIN, "300");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1756,results.size());
    }
    @Test
    public void SearchByCodeBetween() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.CODE_MIN, "200");
        search.addFilter(Filter.CODE_MAX,"300");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1431,results.size());
    }

    @Test
    public void SearchByTermSpring2018() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.addFilter(Filter.TERM, "Spring 2018");
        ArrayList<Course> results = search.executeQuery();
        assertEquals(765,results.size());
    }

    @Test
    public void SearchByTwoTerms() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.addFilter(Filter.TERM, "Spring 2018");
        search.addFilter(Filter.TERM, "Fall 2018");
        ArrayList<Course> results = search.executeQuery();
        assertEquals(0,results.size());
    }

    @Test
    public void SearchByProfessor() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.addFilter(Filter.TERM, "Spring 2020");
        search.addFilter(Filter.PROFESSOR, "trueman");
        ArrayList<Course> results = search.executeQuery();
        assertEquals(3,results.size());
    }

    @Test
    public void FilterByDay() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());

        search.addFilter(Filter.DAY, "TR");
        search.addFilter(Filter.DEPARTMENT, "PSYC");
        ArrayList<Course> results = search.executeQuery();
        assertEquals(34,results.size());
    }

    @Test
    public void SearchByTimeMax() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.TIME_MAX, "9:00:00");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1391,results.size());
    }
    @Test
    public void SearchByTimeBetween() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.TIME_MIN, "11:00:00");
        search.addFilter(Filter.DEPARTMENT,"CHEM");
        search.addFilter(Filter.TIME_MAX,"14:00:00");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(21,results.size());
    }

    @Test
    public void RemoveDeptFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.TERM,"Fall 2019");
        search.addFilter(Filter.DEPARTMENT,"CHEM");
        search.removeFilter(Filter.DEPARTMENT, "CHEM");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(756,results.size());
    }

    @Test
    public void RemoveTimeFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.TIME_MIN, "11:00:00");
        search.addFilter(Filter.DEPARTMENT,"CHEM");
        search.addFilter(Filter.TIME_MAX,"14:00:00");
        search.removeFilter(Filter.TIME_MIN, "11:00:00");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(153, results.size());
    }

    @Test
    public void RemoveCodeFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.CODE_MAX, "200");
        search.addFilter(Filter.DEPARTMENT,"SOCW");
        search.removeFilter(Filter.CODE_MAX,"200");
        search.addFilter(Filter.CODE_MAX, "300");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(19, results.size());
    }

    @Test
    public void RemoveTermFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.CODE_MAX, "300");
        search.addFilter(Filter.TERM,"Fall 2018");
        search.removeFilter(Filter.TERM, "faLL 2018");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(2777, results.size());
    }

    @Test
    public void RemoveDayFilter() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.DAY, "MWF");
        search.removeFilter(Filter.DAY, "MWF");
        search.addFilter(Filter.DAY, "TR");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1127, results.size());
    }
}