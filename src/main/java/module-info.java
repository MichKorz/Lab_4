module javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;
    requires java.sql;

    exports client;

    opens client;
    //opens server;
}