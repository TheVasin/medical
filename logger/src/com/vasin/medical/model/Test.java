package com.vasin.medical.model;

public class Test {

	public static void main(String[] args) throws Exception {
		DataBase db = new DataBase("sa", "", "jdbc:h2:~/medical");
		//db.removeTest(7);
		//db.insertTest("Тестовый тест");
		for(MedicalTest test:db.getAllTests()) {
			System.out.println(test.getID() + ", " + test.getName());
		}
		
		db.getAllRecords();
		
		
	}

}
