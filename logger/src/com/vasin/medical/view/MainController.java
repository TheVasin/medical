package com.vasin.medical.view;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import org.controlsfx.control.CheckComboBox;

import com.vasin.medical.Main;
import com.vasin.medical.model.MedicalTest;
import com.vasin.medical.model.Record;

public class MainController implements javafx.fxml.Initializable {
	private Main mainApp;
	
	@FXML
	CheckComboBox<MedicalTest> testFilter;
	@FXML
	CheckComboBox<MedicalTest> testChooser;
	@FXML
	TableView<Record> mainTable;
	@FXML
	TableView<MedicalTest> testTable;
	@FXML
	TableColumn<Record, String> personColumn;
	@FXML
	TableColumn<Record, String> dateColumn;
	@FXML
	TableColumn<Record, String> testColumn;
	@FXML
	TableColumn<MedicalTest, String> listColumn;
	@FXML
	DatePicker dateInput;
	@FXML
	DatePicker dateFrom;
	@FXML
	DatePicker dateTo;
	@FXML
	TextField inputField;
	@FXML
	TextField addTestField;
	@FXML
	Button addTestButton;
	@FXML
	Button submitButton;
	
	public void setListOfTests(ObservableList<MedicalTest> arg0) {
		testFilter.getItems().setAll(arg0);
		testFilter.getCheckModel().checkAll();
		testFilter.getCheckModel().getCheckedItems().addListener(
				new ListChangeListener<MedicalTest>() {
					@Override
					public void onChanged(ListChangeListener.Change<? extends MedicalTest> arg0) {
						filter();
					}
		});
		testChooser.getItems().setAll(arg0);
	}
	
	public void initTable(ObservableList<Record> arg0) {
		mainTable.setItems(arg0);
		personColumn.setCellValueFactory(cellData -> cellData.getValue().personProperty());
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().takenProperty());
		testColumn.setCellValueFactory(cellData -> cellData.getValue().testProperty());
	}
	
	public void initTestTable(ObservableList<MedicalTest> arg0) {
		testTable.setItems(arg0);
		listColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
	}
	
	public void fillTable(ObservableList<Record> arg0) {
		mainTable.setItems(arg0);
	}
	
	public void addToTable(Record arg0) {
		mainTable.getItems().add(arg0);
	}

	public void addToList(MedicalTest arg0) {
		testTable.getItems().add(arg0);
	}
	
	public void submitData() {
		String name = inputField.getText();
		if (name.isEmpty()) {
			Alert error = new Alert(AlertType.ERROR, "¬ведите пожалуйста им€.");
			error.setHeaderText("ќшибка, пустое им€.");
			error.show();
			return;
		}
		String date = dateInput.getValue().toString();
		ObservableList<MedicalTest> choosed = testChooser.getCheckModel().getCheckedItems();
		if (choosed.isEmpty()) {
			Alert error = new Alert(AlertType.ERROR, "¬ведите пожалуйста анализ.");
			error.setHeaderText("ќшибка, не выбран ни один анализ.");
			error.show();
			return;
		}
		mainApp.submitRecord(name, date, choosed);
		inputField.setText("");
	}
	
	public void addTest() {
		mainApp.addTest(addTestField.getText());
	}
	
	public void startUp() {
		dateInput.setValue(LocalDate.now());	
	}
	
	public void setMainApp(Main app) {
		this.mainApp = app;
	}
	
	public void filterByDate() {
		mainApp.filterDates(dateFrom.getValue(), dateTo.getValue());
		
	}
	
	public void filter() {
		mainApp.filter(dateFrom.getValue(), dateTo.getValue(), testFilter.getCheckModel().getCheckedItems());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
}
