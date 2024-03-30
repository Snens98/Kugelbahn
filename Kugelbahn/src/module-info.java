module Kugelbahn {
	requires javafx.controls;
	requires org.joml;
	requires javafx.fxml;
	
	opens application to javafx.graphics, javafx.fxml;
}
