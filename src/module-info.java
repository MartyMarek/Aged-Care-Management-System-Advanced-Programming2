module ResiCare {
	requires javafx.controls;
	requires javafx.fxml;
	requires java.sql;
	requires javafx.base;
	requires javafx.graphics;
	requires org.junit.jupiter.api;
	
	opens application to javafx.graphics, javafx.fxml, javafx.base;
}
