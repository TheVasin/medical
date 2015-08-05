package com.vasin.medical.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public class MedicalTest {
	private final IntegerProperty id;
	private final StringProperty name;
	
	public MedicalTest(int id, String name) {
		this.id = new SimpleIntegerProperty(id);
		this.name = new SimpleStringProperty(name);
	}
	
	public MedicalTest(String name) {
		this.id = new SimpleIntegerProperty(-1);
		this.name = new SimpleStringProperty(name);
	}
	
	public int getID() {
		return this.id.get();
	}
	
	public IntegerProperty idProperty() {
		return this.id;
	}
	
	public StringProperty nameProperty() {
		return this.name;
	}
	
	public String getName() {
		return this.name.get();
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	public static String parseList(ObservableList<MedicalTest> arg0) {
		String testString = "";
		for (MedicalTest test:arg0) {
			testString = testString + test + ", ";
		}
		return  testString.substring(0, testString.length() - 2);
	}
}
