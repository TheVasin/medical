package com.vasin.medical.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataBase {
	private final String username;
	private final String password;
	private final String database;
	private Connection connection;
	
	public DataBase(String user, String pass, String db) throws ClassNotFoundException {
		this.username = user;
		this.password = pass;
		this.database = db;
		Class.forName("org.h2.Driver");
	}
	
	public Connection getConnection() throws SQLException {
		if (connection != null) return connection;
		return openConnection();
	}
	
	public Connection openConnection() throws SQLException {
		connection = DriverManager.getConnection(database, username, password);
		return connection;
	}
	
	
	public void closeConnection()  throws Exception {
		connection.close();
		connection = null;
	}
	
	public ObservableList<MedicalTest> getAllTests() throws Exception {
		ObservableList<MedicalTest> list = FXCollections.observableArrayList();
		String query = "select * from tests;";
		Statement statement = getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			int id = rs.getInt("ID");
			String name = rs.getString("NAME");
			list.add(new MedicalTest(id, name));
		}
		
		return list;
	}
	
	public ObservableList<Record> getRecordsFiltered(String from, String to, ObservableList<MedicalTest> tests) throws SQLException {
		//select distinct  record_id  from (select record_test.record_id, person, taken, test_id, name from records join record_test on records.id = record_test.record_id join tests on tests.id = record_test.test_id where taken between '2015-05-23' and '2015-07-16') where test_id = 1 or test_id = 2;
		String query = new String("select * from records where id in (select distinct record_id from (select record_test.record_id, person, taken, test_id, name"
				+ " from records join record_test on records.id = record_test.record_id join tests on tests.id = record_test.test_id"
				+ " where taken between '" + from + "' and '" + to + "') where ");
		int i = 0;
		for (MedicalTest test:tests) {
			if (i > 0) { query = query +" or "; }
			query = query + "test_id = " + test.getID();
			i++;
		}
		query = query + ");";
		Statement statement = getConnection().createStatement();
		ObservableList<Record> records = FXCollections.observableArrayList();
		ResultSet rs = statement.executeQuery(query);
		
		while (rs.next()) {
			int id = rs.getInt("ID");
			String person = rs.getString("PERSON");
			String taken = rs.getString("TAKEN");
			String test_string = rs.getString("TESTS");
			records.add(new Record(id, person, taken, test_string));
		}
		return records;
	}
	
	public ObservableList<Record> getAllRecords() throws SQLException {
		String query = new String("select * from records;");
		Statement statement = getConnection().createStatement();
		ObservableList<Record> records = FXCollections.observableArrayList();
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			int id = rs.getInt("ID");
			String person = rs.getString("PERSON");
			String taken = rs.getString("TAKEN");
			String tests = rs.getString("TESTS");
			records.add(new Record(id, person, taken,tests));
		}
		
		return records;
	}
	
	public void insertRecord(String name, String date, ObservableList<MedicalTest> tests) throws SQLException {
		String testString = MedicalTest.parseList(tests);
		String query = new String("insert into records(person, taken, tests) values('" + name + "', '" + date + "', '" + testString + "');");
		Statement statement = getConnection().createStatement();
		statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
		ResultSet rs = statement.getGeneratedKeys();
		int index = -1;
		while (rs.next()) {
			index = rs.getInt(1);
		}
		String q = "";
		for (MedicalTest test:tests) {
			q = q + "insert into record_test(record_id, test_id) values(" + index + ", " + test.getID() + ");";
		}
		Statement st = getConnection().createStatement();
		st.executeUpdate(q);
	}
	
	public ObservableList<Record> getRecordsByDate(String from, String to) throws SQLException {
		String query = new String("select * from records where taken between '" + from + "' and '" + to + "';");
		Statement statement = getConnection().createStatement();
		ResultSet rs = statement.executeQuery(query);
		ObservableList<Record> records = FXCollections.observableArrayList();
		while(rs.next()) {
			int id = rs.getInt("ID");
			String person = rs.getString("PERSON");
			String taken = rs.getString("TAKEN");
			String tests = rs.getString("TESTS");
			records.add(new Record(id, person, taken, tests));
		}
		return records;
	}
	
	public ObservableList<Record> getRecords() throws SQLException {
		String query = new String("select record_test.record_id, person, taken, records.tests, "
				+ "name from records join record_test on records.id = record_test.record_id "
				+ "join tests on tests.id = record_test.test_id;");
		Statement statement = getConnection().createStatement();
		ObservableList<Record> records = FXCollections.observableArrayList();
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()) {
			int id = rs.getInt("RECORD_ID");
			String person = rs.getString("PERSON");
			String taken = rs.getString("TAKEN");
			String tests = rs.getString("TESTS");
			records.add(new Record(id, person, taken, tests));
		}
		return records;
	}
	
	public int insertTest(String name) throws SQLException {
		String query = new String("insert into TESTS(NAME) values('" + name + "');");
		Statement statement = getConnection().createStatement();
		return statement.executeUpdate(query);
	}
	
	public int removeTest(int id) throws SQLException {
		String query = new String("DELETE FROM TESTS WHERE ID=" + id + ";");
		Statement statement = getConnection().createStatement();
		return statement.executeUpdate(query);
	}
	
	public int removeRecord(int id) throws SQLException {
		String query = new String("DELETE FROM RECORDS WHERE ID=" + id + ";");
		Statement statement = getConnection().createStatement();
		return statement.executeUpdate(query);
	}
	
}
