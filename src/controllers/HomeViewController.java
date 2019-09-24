package controllers;

import java.sql.Date;
import java.sql.Time;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;

import application.Task;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

public class HomeViewController implements Initializable{

    @FXML
    private TableView<Task> upcomingTable;

    @FXML
    private TableColumn<Task, String> uTitle;

    @FXML
    private TableColumn<Task, Date> usDate;

    @FXML
    private TableColumn<Task, Date> udDate;

    @FXML
    private JFXTextField uTitleText;

    @FXML
    private JFXTextField uLocation;

    @FXML
    private JFXDatePicker uStartDate;

    @FXML
    private JFXTimePicker uStartTime;

    @FXML
    private JFXDatePicker uEndDate;

    @FXML
    private JFXTimePicker uEndTime;

    @FXML
    private JFXButton save;

    @FXML
    private JFXTextArea uDetails;

    @FXML
    private JFXTextField nTitle;

    @FXML
    private JFXTextField nLocation;

    @FXML
    private JFXDatePicker nStartDate;

    @FXML
    private JFXTimePicker nStartTime;

    @FXML
    private JFXDatePicker nEndDate;

    @FXML
    private JFXTimePicker nEndTime;

    @FXML
    private JFXTextArea nDetails;

    @FXML
    private TableView<Task> nTasksTable;

    @FXML
    private TableColumn<Task, String> nTitleCol;

    @FXML
    private TableColumn<Task, Date> nStartDateCol;

    @FXML
    private TableColumn<Task, Date> nDueDateCol;

    @FXML
    private TableColumn<Task, String> nDetailsCol;

    @FXML
    private TableView<Task> completTable;

    @FXML
    private TableColumn<Task, String> cTitleCol;

    @FXML
    private TableColumn<Task, Date> cStartDateCol;

    @FXML
    private TableColumn<Task, Date> cDueDateCol;

    @FXML
    private JFXTextField cTitle;

    @FXML
    private JFXTextField cLocation;

    @FXML
    private JFXDatePicker cStartDate;

    @FXML
    private JFXTimePicker cStartTime;

    @FXML
    private JFXDatePicker cEndDate;

    @FXML
    private JFXTimePicker cEndTime;

    @FXML
    private JFXTextArea cDetails;
    
    @FXML
    private TextField search;
    
    @FXML
    private TextField searchUTask;
    
    private PropertyValueFactory<Task,String> titleFactory = new PropertyValueFactory<>("title");
    private PropertyValueFactory<Task,Date> startFactory = new PropertyValueFactory<>("start");
    private PropertyValueFactory<Task,Date> stopFactory = new PropertyValueFactory<>("stop");
    private PropertyValueFactory<Task,String> detailFactory = new PropertyValueFactory<>("details");
    private Alert alert = new Alert(AlertType.ERROR);
    private Alert success = new Alert(AlertType.INFORMATION,"SUCCESS!");
    private TextInputDialog email= new TextInputDialog();
    private final Date today = new Date(System.currentTimeMillis());
    
    private Operations operation;
    private Task task;
    private Time now;
    
    @Override
    public void initialize(URL url, ResourceBundle resources) {
    	setupTables();
    	changeMode(false);
    	
    	try {
    		operation = new Operations();
    		operation.updateTasks();
    		loadTableData();
    	}catch(Exception ex) {
    		alert.setContentText("Error loading program data");
    		alert.setHeaderText("LOAD ERROR");
    		alert.showAndWait();
    	}
    	email.setHeaderText("EMAIL INVITATION");
    	email.setContentText("Enter receiver's email");
    	success.setHeaderText(null);
    }
    
    private void setupTables() {
    	//Upcoming tab table view
    	uTitle.setCellValueFactory(titleFactory);
    	usDate.setCellValueFactory(startFactory);
    	udDate.setCellValueFactory(stopFactory);
    	upcomingTable.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
    		task = upcomingTable.getSelectionModel().getSelectedItem();
    		if(task!=null) {
    			uTitleText.setText(task.getTitle());
    			uDetails.setText(task.getDetails());
    			uLocation.setText(task.getLocation());
    			uStartDate.setValue(task.getStart().toLocalDate());
    			uEndDate.setValue(task.getStop().toLocalDate());
    			uStartTime.setValue(task.getStartTime().toLocalTime());
    			uEndTime.setValue(task.getEndTime().toLocalTime());
    		}
    	});
    	
    	//All tasks tab tableview
    	nTitleCol.setCellValueFactory(titleFactory);
    	nStartDateCol.setCellValueFactory(startFactory);
    	nDueDateCol.setCellValueFactory(stopFactory);
    	nDetailsCol.setCellValueFactory(detailFactory);
    	
    	//Completed tasks tableview
    	cTitleCol.setCellValueFactory(titleFactory);
    	cStartDateCol.setCellValueFactory(startFactory);
    	cDueDateCol.setCellValueFactory(stopFactory);
    	completTable.getSelectionModel().selectedItemProperty().addListener((obs,ov,nv)->{
    		task = completTable.getSelectionModel().getSelectedItem();
    		if(task!=null) {
    			cTitle.setText(task.getTitle());
    			cDetails.setText(task.getDetails());
    			cLocation.setText(task.getLocation());
    			cStartDate.setValue(task.getStart().toLocalDate());
    			cEndDate.setValue(task.getStop().toLocalDate());
    			cStartTime.setValue(task.getStartTime().toLocalTime());
    			cEndTime.setValue(task.getEndTime().toLocalTime());
    		}
    	});
    }
    
    private void loadTableData() {
    	upcomingTable.setItems(operation.readUpcoming());
    	nTasksTable.setItems(operation.readAllTasks());
    	completTable.setItems(operation.readCompletTasks());
    }

    @FXML
    void deleteCompTask(ActionEvent event) {
    	task = completTable.getSelectionModel().getSelectedItem();
    	if(task!=null){
    		if(operation.deleteTask(task.getCode())) {
    			completTable.getItems().remove(task);
    			loadTableData();
    		}
    	}
    }

    @FXML
    void deleteTask(ActionEvent event) {
    	task = upcomingTable.getSelectionModel().getSelectedItem();
    	if(task!=null){
    		if(operation.deleteTask(task.getCode())) {
    			upcomingTable.getItems().remove(task);
    			uTitleText.clear();
    			uDetails.clear();
    			uLocation.clear();
    			uStartDate.setValue(null);
    			uEndDate.setValue(null);
    			uStartTime.setValue(null);
    			uEndTime.setValue(null);
    			loadTableData();
    			success.showAndWait();
    		}
    	}
    }

    @FXML
    void editTask(ActionEvent event) {
    	task = upcomingTable.getSelectionModel().getSelectedItem();
		if(task!=null)
			changeMode(true);
    }

    private void changeMode(boolean b) {
		uTitleText.setEditable(b);
		uDetails.setEditable(b);
		uLocation.setEditable(b);
		uStartDate.setDisable(!b);
		uEndDate.setDisable(!b);
		uStartTime.setDisable(!b);
		uEndTime.setDisable(!b);
		save.setVisible(b);
		if(!b) {
			uStartTime.setStyle("-fx-opacity: 1");
			uEndTime.setStyle("-fx-opacity: 1");
			uStartDate.setStyle("-fx-opacity: 1");
			uEndDate.setStyle("-fx-opacity: 1");
			uStartTime.getEditor().setStyle("-fx-opacity: 1");
			uEndDate.getEditor().setStyle("-fx-opacity: 1");
			uStartDate.getEditor().setStyle("-fx-opacity: 1");
			uEndTime.getEditor().setStyle("-fx-opacity: 1");
		}
	}

	@FXML
    void markCompleted(ActionEvent event) {
		task = upcomingTable.getSelectionModel().getSelectedItem();
    	if(task!=null){
    		if(operation.markCompleted(task.getCode())) {
    			upcomingTable.getItems().remove(task);
    			loadTableData();
    			success.showAndWait();
    		}
    	}
    }

    @FXML
    void saveNewTask(ActionEvent event) {
    	if(nTitle.getText().isEmpty()||nLocation.getText().isEmpty()||nDetails.getText().isEmpty()||
    			nStartDate.getValue()==null||nStartTime.getValue()==null||nEndDate.getValue()==null||
    			nEndTime.getValue()==null)
    		return;
    	else if(Date.valueOf(nEndDate.getValue()).before(new Date(System.currentTimeMillis()))) {
    		alert.setHeaderText("DATE ERROR");
    		alert.setContentText("The ending date has already passed!");
    		alert.showAndWait();
    	}
    	else if(Date.valueOf(nEndDate.getValue()).before(Date.valueOf(nStartDate.getValue()))&&!Date.valueOf(nEndDate.getValue()).equals(Date.valueOf(nStartDate.getValue()))){
    		alert.setHeaderText("DATE ERROR");
    		alert.setContentText("The ending date cannot be before the starting date");
    		alert.showAndWait();
    	}else if(Time.valueOf(nEndTime.getValue()).before(Time.valueOf(nStartTime.getValue()))&&nEndDate.getValue().equals(nStartDate.getValue())) {
    		alert.setHeaderText("TIME ERROR");
    		alert.setContentText("The ending time cannot be before the starting time");
    		alert.showAndWait();
    	}
    	else {
    		task = new Task(computeCode(),nTitle.getText(),nDetails.getText(),nLocation.getText(),Date.valueOf(nStartDate.getValue()),Date.valueOf(nEndDate.getValue()),Time.valueOf(nStartTime.getValue()),Time.valueOf(nEndTime.getValue()),0);
    		
    		if(operation.saveNewTask(task)) {
    			nTitle.clear();
    			nDetails.clear();
    			nLocation.clear();
    			nStartDate.setValue(null);
    			nEndDate.setValue(null);
    			nStartTime.setValue(null);
    			nEndTime.setValue(null);
    			loadTableData();
    			success.showAndWait();
    		}
    	}
    }

    @FXML
    void saveTaskEdit(ActionEvent event) {
    	if(uTitleText.getText().isEmpty()||uLocation.getText().isEmpty()||uDetails.getText().isEmpty()||
    			uStartDate.getValue()==null||uStartTime.getValue()==null||uEndDate.getValue()==null||
    			uEndTime.getValue()==null)
    		return;
    	else if(Date.valueOf(uEndDate.getValue()).before(new Date(System.currentTimeMillis()))) {
    		alert.setHeaderText("DATE ERROR");
    		alert.setContentText("The ending date has already passed!");
    		alert.showAndWait();
    	}
    	else if(Date.valueOf(uEndDate.getValue()).before(Date.valueOf(uStartDate.getValue()))&&!Date.valueOf(uEndDate.getValue()).equals(Date.valueOf(uStartDate.getValue()))){
    		alert.setHeaderText("DATE ERROR");
    		alert.setContentText("The ending date cannot be before the starting date");
    		alert.showAndWait();
    	}else if(Time.valueOf(uEndTime.getValue()).before(Time.valueOf(uStartTime.getValue()))&&uEndDate.getValue().equals(uStartDate.getValue())) {
    		alert.setHeaderText("TIME ERROR");
    		alert.setContentText("The ending time cannot be before the starting time");
    		alert.showAndWait();
    	}
    	else {
    		String code = upcomingTable.getSelectionModel().getSelectedItem().getCode();
    		task = new Task(code,uTitleText.getText(),uDetails.getText(),uLocation.getText(),Date.valueOf(uStartDate.getValue()),
    				Date.valueOf(uEndDate.getValue()),Time.valueOf(uStartTime.getValue()),Time.valueOf(uEndTime.getValue()),0);
    		
    		if(operation.saveTaskEdit(task)) {
    			uTitleText.clear();
    			uDetails.clear();
    			uLocation.clear();
    			uStartDate.setValue(null);
    			uEndDate.setValue(null);
    			uStartTime.setValue(null);
    			uEndTime.setValue(null);
    			loadTableData();
    			changeMode(false);
    			success.showAndWait();
    		}
    	}
    }

    @FXML
    void sendMail(ActionEvent event) {
    	final Task tas = upcomingTable.getSelectionModel().getSelectedItem();
    	if(tas!=null){
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
    				Optional<String> mail = email.showAndWait();
    	    		if(mail.isPresent()){
    	    			String message = tas.getDetails()+"\nVenue "+tas.getLocation();
    	    			message += "\nStart time : "+tas.getStart().toString()+" "+tas.getStartTime().toString();
    	    			message += "\nEnd time :"+tas.getStop()+" "+tas.getEndTime().toString();
    	    			message +="\n\n\n----------------------------------------------------";
    	    			message +="\nMessage from *Planner Application*";
    	    			if(EmailSender.sendMail(mail.get(), tas.getTitle(), message)) {
    	    				success.showAndWait();
    	    			}else {
    	    				alert.setHeaderText("EMAIL ERROR");
    	    				alert.setContentText("Email sending failed");
    	    				alert.showAndWait();
    	    			}
    	    		}
				}
			});
    	}
    }
    
    @FXML
    void searchTask(KeyEvent key) {
    	String type = search.getText()+key.getCharacter();
    	if(type.isEmpty()||(type.equals(""))||(type.equals(" ")))
    		nTasksTable.setItems(operation.readAllTasks());
    	else {
    		if(type.endsWith(""))
    			type = type.substring(0, type.length()-1);

        	nTasksTable.setItems(operation.searchTasks(type));
    	}
    }
    
    @FXML
    void searchUTask(KeyEvent key) {
    	String type = searchUTask.getText()+key.getCharacter();
    	if(type.isEmpty()||(type.equals(""))||(type.equals(" ")))
    		upcomingTable.setItems(operation.readUpcoming());
    	else {
    		if(type.endsWith(""))
    			type = type.substring(0, type.length()-1);

        	upcomingTable.setItems(operation.searchUpcomingTasks(type));
    	}
    }
    
    private String computeCode() {
    	now = new Time(System.currentTimeMillis());
    	String code = "TSK-"+today.toString()+"-"+now.toString();
    	
    	return code;
    }
    
    @FXML
    private void switchToEnglish() {
    	try {
    		FileWriter writer = new FileWriter("Database/lang.properties");
    		Properties lang = new Properties();
    		lang.setProperty("locale", "en");
    		lang.store(writer, "Planner application default language");
    		writer.close();
    	}catch(IOException ex) {
    		
    	}
    }
    
    @FXML
    private void switchToFrench() {
    	try {
    		FileWriter writer = new FileWriter("Database/lang.properties");
    		Properties lang = new Properties();
    		lang.setProperty("locale", "fr");
    		lang.store(writer, "Planner application default language");
    		writer.close();
    	}catch(IOException ex) {
    		
    	}
    }
    
    public void closeConnection() {
    	operation.closeConnection();
    }
}