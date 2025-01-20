module javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires jdk.compiler;

    exports client;

    opens client;
    //opens server;
}