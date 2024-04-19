package bugbusters;

import org.junit.jupiter.api.Test;

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
    public void SearchByCodeMin() {
        Registrar registrar = new Registrar("schemaBugBuster","u222222","p222222");
        DatabaseSearch search = new DatabaseSearch(registrar.getConn());
        search.addFilter(Filter.CODE_MIN, "300");

        ArrayList<Course> results = search.executeQuery();
        assertEquals(1756,results.size());
    }
}