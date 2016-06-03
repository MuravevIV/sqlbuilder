package com.ilyamur.sqlbuilder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

public class SqlBuilderTest {

    private SqlBuilder target;

    @Before
    public void before() {
        target = new SqlBuilder();
    }

    @Test
    public void initialState_throws() {
        try {
            target.toSql();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(SqlBuilder.FROM_EXPRESSION_NOT_DEFINED, e.getMessage());
        }
    }

    @Test
    public void selectNotDefined_throws() {
        try {
            target.from("employee").toSql();
            fail();
        } catch (IllegalStateException e) {
            assertEquals(SqlBuilder.SELECT_EXPRESSION_NOT_DEFINED, e.getMessage());
        }
    }

    @Test
    public void singleSelect() {
        String query = target.select("name").from("employee").toSql();
        assertEquals("SELECT name FROM employee", query);
    }

    @Test
    public void multipleSelectVararg() {
        String query = target.select("name", "sname").from("employee").toSql();
        assertEquals("SELECT name, sname FROM employee", query);
    }

    @Test
    public void multipleSelectChain() {
        String query = target.select("name").select("sname").from("employee").toSql();
        assertEquals("SELECT name, sname FROM employee", query);
    }

    @Test
    public void singleWhereEqualsNoQuoted() {
        String query = target.select("name").from("employee").whereEquals("id_employee", 1).toSql();
        assertEquals("SELECT name FROM employee WHERE id_employee = 1", query);
    }

    @Test
    public void singleWhereEqualsQuotedString() {
        String query = target.select("name").from("employee").whereEquals("sname", "Smith").toSql();
        assertEquals("SELECT name FROM employee WHERE sname = 'Smith'", query);
    }

    @Test
    public void singleWhereEqualsQuotedChar() {
        String query = target.select("name").from("employee").whereEquals("sname", 'S').toSql();
        assertEquals("SELECT name FROM employee WHERE sname = 'S'", query);
    }

    @Test
    public void multipleWhereEquals() {
        String query = target.select("name").from("employee")
                .whereEquals("id_employee", 1)
                .whereEquals("sname", "Smith")
                .toSql();
        assertEquals("SELECT name FROM employee WHERE id_employee = 1 AND sname = 'Smith'", query);
    }
}



























