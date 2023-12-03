module com.aw8361 {
    requires transitive javafx.controls;
    requires javafx.fxml;

    opens com.aw8361 to javafx.fxml;
    opens com.aw8361.app to javafx.fxml;
    exports com.aw8361;
}
