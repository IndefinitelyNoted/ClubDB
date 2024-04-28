
package business;

import java.sql.*;
import javax.naming.Context;
import javax.sql.DataSource;
import javax.naming.InitialContext;
import javax.naming.NamingException;
/**
 * @author pdaniel
 */
public class ConnectionPool {
    private static ConnectionPool pool = null;
    private static DataSource dataSource = null;
    
    private ConnectionPool() {
        try {
            InitialContext ic = new InitialContext();
            Context envCtx = (Context) ic.lookup("java:comp/env");
            dataSource = (DataSource) envCtx.lookup("jdbc/clubdb");
            
        } catch (NamingException e) {
            System.out.println(e);
        }
    }
    
    public static synchronized ConnectionPool getInstance() {
        if (pool == null) {
            pool = new ConnectionPool();
        }
        return pool;
    }
    
    public Connection getConnection() {
        try {
            return dataSource.getConnection();            
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }
    
    public void freeConnection(Connection c) {
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println(e);
        }
    }
    
}
