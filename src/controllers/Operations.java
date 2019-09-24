package controllers;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

import application.Task;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Operations {
	private static final String INSERT = "INSERT INTO tasks VALUES(?,?,?,?,?,?,?,?,?)";
	private static final String DELETE = "UPDATE tasks set status=2 WHERE code = ?";
	private static final String COMPLETE = "UPDATE tasks SET status=1 WHERE code =?";
	private static final String SEARCH = "SELECT * FROM tasks WHERE title LIKE ? AND status!=2 ORDER BY endT ASC";
	private static final String SEARCHU = "SELECT * FROM tasks WHERE title LIKE ? AND status=0 ORDER BY endT ASC";
	private static final String UPDATE = "UPDATE tasks SET title=?, detail=?, location=?,"
			+ "startD=?, endD=?, startT=?, endT=? WHERE code=?";
	
	private Connection connect;
	private Statement statement;
	private PreparedStatement insert,delete,complete,search,searchU,update;
	private Task task;
	private ResultSet set;
	private String request;
	private static final String today = "'"+(new Date(System.currentTimeMillis())).toString()+"'";
	
	public Operations() throws SQLException, IOException{
		DriverManager.registerDriver(new org.sqlite.JDBC());
		connect = DriverManager.getConnection("jdbc:sqlite:Database/Planner.db");
		connect.setAutoCommit(true);
		statement = connect.createStatement();
		if(!checkDatabase())
			createDatabase();
		
		insert = connect.prepareStatement(INSERT);
		delete = connect.prepareStatement(DELETE);
		complete = connect.prepareStatement(COMPLETE);
		search = connect.prepareStatement(SEARCH);
		update = connect.prepareStatement(UPDATE);
		searchU = connect.prepareStatement(SEARCHU);
	}
	
	public void updateTasks() {
		try {
			statement.executeUpdate("UPDATE tasks SET status=1 WHERE endD<"+today);
		}catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void closeConnection() {
		try {
			connect.close();
		}catch(Exception ex) {
			System.exit(1);
		}
	}
	
	private boolean checkDatabase() {
		try {
			set = statement.executeQuery("SELECT * FROM tasks");
			set.next();
			return true;
		}catch(SQLException ex) {
			ex.printStackTrace();
			return false;
		}
	}
	
	private void createDatabase() throws SQLException{
		statement.execute("CREATE TABLE IF NOT EXISTS tasks(code VARCHAR(30) PRIMARY KEY,"
				+ " title VARCHAR(50),detail TEXT,location TEXT, "
				+ "startD DATE, endD DATE, startT TIME, endT TIME,status INT(3))");
	}
	
	public void checkAndDeleteDbFile() {
		File db = new File("Database/Planner.db");
		if(db.exists())
			db.delete();
	}
	
	public ObservableList<Task> readAllTasks(){
		ObservableList<Task> list = FXCollections.observableArrayList();
		request = "SELECT * FROM tasks WHERE status!=2 ORDER BY endT ASC";
		try {
			set = statement.executeQuery(request);
			while(set.next()) {
				task = new Task(set.getString("code"),set.getString("title"),
						set.getString("detail"),set.getString("location"),Date.valueOf(set.getString("startD")),
						Date.valueOf(set.getString("endD")),
						Time.valueOf(set.getString("startT")),Time.valueOf(set.getString("endT")),set.getInt("status"));
				list.add(task);
			}
		}catch(SQLException ex) {
		}
		
		return list;
	}

	public ObservableList<Task> readUpcoming() {
		ObservableList<Task> list = FXCollections.observableArrayList();
		request = "SELECT * FROM tasks WHERE endD>="+today+" AND status=0 ORDER BY endT ASC";
		try {
			set = statement.executeQuery(request);
			while(set.next()) {
				task = new Task(set.getString("code"),set.getString("title"),
						set.getString("detail"),set.getString("location"),Date.valueOf(set.getString("startD")),
						Date.valueOf(set.getString("endD")),
						Time.valueOf(set.getString("startT")),Time.valueOf(set.getString("endT")),set.getInt("status"));
				list.add(task);
			}
		}catch(SQLException ex) {
		}
		
		return list;
	}

	public ObservableList<Task> readCompletTasks() {
		ObservableList<Task> list = FXCollections.observableArrayList();
		request = "SELECT * FROM tasks WHERE status=1 ORDER BY endT ASC";
		try {
			set = statement.executeQuery(request);
			while(set.next()) {
				task = new Task(set.getString("code"),set.getString("title"),
						set.getString("detail"),set.getString("location"),Date.valueOf(set.getString("startD")),
						Date.valueOf(set.getString("endD")),
						Time.valueOf(set.getString("startT")),Time.valueOf(set.getString("endT")),set.getInt("status"));
				list.add(task);
			}
		}catch(SQLException ex) {
		}
		
		return list;
	}

	public boolean deleteTask(String code) {
		try {
			delete.setString(1, code);
			return !delete.execute();
		}catch(SQLException ex) {
			return false;
		}
	}

	public boolean saveNewTask(Task task) {
		 try {
			 insert.setString(1, task.getCode());
			 insert.setString(2, task.getTitle());
			 insert.setString(3, task.getDetails());
			 insert.setString(4, task.getLocation());
			 insert.setString(5, task.getStart().toString());
			 insert.setString(6, task.getStop().toString());
			 insert.setString(7, task.getStartTime().toString());
			 insert.setString(8, task.getEndTime().toString());
			 insert.setInt(9, task.getStatus());
			 
			 insert.execute();
			 return true;
		 }catch(SQLException ex) {
			 return false;
		 }
	}
	
	public boolean saveTaskEdit(Task task) {
		try {
			 update.setString(1, task.getTitle());
			 update.setString(2, task.getDetails());
			 update.setString(3, task.getLocation());
			 update.setString(4, task.getStart().toString());
			 update.setString(5, task.getStop().toString());
			 update.setString(6, task.getStartTime().toString());
			 update.setString(7, task.getEndTime().toString());
			 update.setString(8, task.getCode());
			 
			 update.execute();
			 return true;
		 }catch(SQLException ex) {
			 return false;
		 }
	}

	public ObservableList<Task> searchTasks(String type) {
		ObservableList<Task> list = FXCollections.observableArrayList();
		try {
			search.setString(1, "%"+type+"%");
			set = search.executeQuery();
			while(set.next()) {
				task = new Task(set.getString("code"),set.getString("title"),
						set.getString("detail"),set.getString("location"),Date.valueOf(set.getString("startD")),
						Date.valueOf(set.getString("endD")),
						Time.valueOf(set.getString("startT")),Time.valueOf(set.getString("endT")),set.getInt("status"));
				list.add(task);
			}
		}catch(SQLException ex) {
		}
		
		return list;
	}

	public boolean markCompleted(String code) {
		try {
			complete.setString(1, code);
			return !complete.execute();
		}catch(SQLException ex) {
			return false;
		}
	}

	public ObservableList<Task> searchUpcomingTasks(String type) {
		ObservableList<Task> list = FXCollections.observableArrayList();
		try {
			searchU.setString(1, "%"+type+"%");
			set = searchU.executeQuery();
			while(set.next()) {
				task = new Task(set.getString("code"),set.getString("title"),
						set.getString("detail"),set.getString("location"),Date.valueOf(set.getString("startD")),
						Date.valueOf(set.getString("endD")),
						Time.valueOf(set.getString("startT")),Time.valueOf(set.getString("endT")),set.getInt("status"));
				list.add(task);
			}
		}catch(SQLException ex) {
		}
		
		return list;
	}
	
	
}
