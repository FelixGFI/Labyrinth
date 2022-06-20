module com.example.labyrinth {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.labyrinth to javafx.fxml;
    exports com.example.labyrinth;
}