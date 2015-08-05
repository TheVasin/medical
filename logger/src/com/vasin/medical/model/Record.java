package com.vasin.medical.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Record {
	private final IntegerProperty id;
	private final StringProperty person;
	private final StringProperty taken;
	private final StringProperty tests;
	
	public Record(int id, String person, String taken, String tests) {
		this.id = new SimpleIntegerProperty(id);
		this.person = new SimpleStringProperty(person);
		this.taken = new SimpleStringProperty(taken);
		this.tests = new SimpleStringProperty(tests);
	}
	
	public Record(String person, String taken, String tests) {
		this.id = new SimpleIntegerProperty(-1);
		this.person = new SimpleStringProperty(person);
		this.taken = new SimpleStringProperty(taken);
		this.tests = new SimpleStringProperty(tests);
	}
	
	public int getID() {
		return id.get();
	}
	
	
	public IntegerProperty idProperty() {
		return id;
	}
	
	public StringProperty personProperty() {
		return person;
	}
	
	public StringProperty takenProperty() {
		return taken;
	}
	
	public StringProperty testProperty() {
		return tests;
	}
}
