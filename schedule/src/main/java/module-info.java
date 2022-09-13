module com.table.schedule {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires tornadofx.controls;
    requires com.h2database;
    requires javafx.swing;

    opens com.table.schedule to javafx.fxml;
    exports com.table.schedule;
}