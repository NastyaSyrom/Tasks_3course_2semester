module com.example.taskweb {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.taskweb to javafx.fxml;
    exports com.example.taskweb;
}