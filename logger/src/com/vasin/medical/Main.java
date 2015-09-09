package com.vasin.medical;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

import com.vasin.medical.model.DataBase;
import com.vasin.medical.model.MedicalTest;
import com.vasin.medical.model.Record;
import com.vasin.medical.view.MainController;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
	
	private Stage primaryStage;
	private MainController controller;
	private DataBase db;
	
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Учет анализов");
		this.primaryStage.centerOnScreen();
		this.primaryStage.setHeight(500);
		this.primaryStage.setWidth(800);
		this.primaryStage.setMinHeight(500);
		this.primaryStage.setMinWidth(800);
		
		try {
			initDB();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		initMainView();	
		
		try {
			controller.setListOfTests(db.getAllTests());
			controller.initTable(db.getAllRecords());
			controller.initTestTable(db.getAllTests());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void initDB() throws Exception {
		db = new DataBase("MEDICAL_ADMIN", "gj264jgb63jn", "jdbc:h2:~/medical");
	}
	
	public DataBase getDB() {
		return db;
	}
	
	public void submitRecord(String name, String date, ObservableList<MedicalTest> tests) {
		try {
			db.insertRecord(name, date, tests);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateTable(new Record(name, date, MedicalTest.parseList(tests)));
	}
	
	public void addTest(String name) {
		try {
			db.insertTest(name);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		updateTestList(new MedicalTest(name));
		try {
			controller.setListOfTests(db.getAllTests());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateTable() {
		try {
			controller.fillTable(db.getAllRecords());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateTable(Record arg0) {
		controller.addToTable(arg0);
	}
	
	private void updateTestList(MedicalTest arg0) {
		controller.addToList(arg0);
	}
	
	public void filterDates(LocalDate from, LocalDate to) {
		
		try {
			ObservableList<Record> records = db.getRecordsByDate(from.toString(), to.toString());
			controller.fillTable(records);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void initMainView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/MainView.fxml"));
			AnchorPane mainView = (AnchorPane) loader.load();
			Scene scene = new Scene(mainView);
			primaryStage.setScene(scene);
			primaryStage.show();
			controller = loader.getController();
			controller.setMainApp(this);
			controller.startUp();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
		
	public static void main(String[] args) {
		launch(args);
	}

	public void filter(LocalDate arg0, LocalDate arg1,
			ObservableList<MedicalTest> arg2) {
		if (arg0 == null) { return; }
		if (arg1 == null) { return; }
		try {
			ObservableList<Record> records = db.getRecordsFiltered(arg0.toString(), arg1.toString(), arg2);
			controller.fillTable(records);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
