package bugbusters;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseSearchTest {

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
        search.addFilter(Filter.ID, "444");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1,results.size());
        assertEquals(444, results.get(0).getId());
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
    public void SearchByMultipleWords() {
        //TODO: test as full name then by keyword
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.NAME, "PRINCIPLES OF ACCOUNTING");

        ArrayList<Course> results = search.executeQuery();
//        assertEquals(3,results.size());
        Run.printCourses(results);
    }
}