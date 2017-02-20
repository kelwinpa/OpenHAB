package Storage;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class Storage {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://localhost/smarthome?useSSL=false";
	
	//  Database credentials
    private final String USER = "root";
    private final String PASS = "";
    private Connection conn = null;
    private Statement stmt = null;
    
    public Storage() {
    	   try{
    	      //STEP 2: Register JDBC driver
    	      Class.forName("com.mysql.jdbc.Driver");

    	      //STEP 3: Open a connection
    	      conn = DriverManager.getConnection(DB_URL,USER,PASS);
    	      
    	   }catch(SQLException se){
    	      //Handle errors for JDBC
    	      se.printStackTrace();
    	   }catch(Exception e){
    	      //Handle errors for Class.forName
    	      e.printStackTrace();
    	   }
    }
    
	public void write(String table, String fields, String what) {
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "INSERT INTO " + table + " (" + fields + ") VALUES (" + what + ")";//('John', 'Doe', 'john@example.com')";
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
	}
	
	/**
	 * Overwrites a single row, passing the integer id
	 * @param table table name
	 * @param field SINGLE field
	 * @param newvalue new value
	 * @param id Integer ID of the row
	 */
	public void overwrite(String table, String field, String newvalue, int id) {
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "UPDATE " + table + " SET " + field + "=\"" + newvalue + "\" WHERE id=" + id;
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		/*UPDATE tutorials_tbl
        SET tutorial_title="Learning JAVA"
        WHERE tutorial_id=3*/
	}
	
	/**
	 * Overwrites a single row, passing an hand-written wherecond (e.g., "WHERE id=3")
	 * @param table table name
	 * @param field SINGLE field
	 * @param newvalue new value
	 * @param wherecond condition to find the single row (must begin with WHERE)
	 */
	public void overwrite(String table, String field, String newvalue, String wherecond) {
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "UPDATE " + table + " SET " + field + "=\"" + newvalue + "\" " + wherecond;
			stmt.executeUpdate(sql);
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		/*UPDATE tutorials_tbl
        SET tutorial_title="Learning JAVA"
        WHERE tutorial_id=3*/
	}
	
	/**
	 * Reads from DB from table table the field fieldname
	 * @param table table name
	 * @param fieldname name of field
	 * @param where where condition
	 * @return an Object, which you can convert to int, string, date or whatever you want
	 */
	public Object read(String table, String fieldname, String where) {
		while(otherWriting());
		List<Object> l = this.readMany(table, fieldname, where);
		if(l != null && !l.isEmpty())
			return l.get(0);
		return null;
	}
	
	/**
	 * Returns true if MySQL is beeing written
	 * @return true or false
	 */
	private boolean otherWriting() {
		boolean r = false;
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "show open tables where In_use > 0";
			ResultSet set = stmt.executeQuery(sql);
			if(set.next())
				r = true;
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * Reads from DB from table table the field fieldname.
	 * Example: new Storage().readMany("light", "id", "WHERE room=\"living room\"");
	 * @param table table name
	 * @param fieldname name of field
	 * @param where where condition
	 * @return many Objects, which you can convert to int, string, date or whatever you want
	 */
	public List<Object> readMany(String table, String fieldname, String where) {
		List<Object> r = new LinkedList<Object>();
		try {
			stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM " + table + " " + where;
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){
				r.add(rs.getObject(fieldname));
			}
			rs.close();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			this.close();
		}
		return r;
	}
	
	public void close() {
		try {
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
