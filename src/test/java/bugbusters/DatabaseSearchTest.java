package bugbusters;

import org.junit.jupiter.api.Test;

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
        search.addFilter(SearchFilter.Searchable.DEPT, "HUMA");
        Run.printCourses(search.executeQuery());
    }
}