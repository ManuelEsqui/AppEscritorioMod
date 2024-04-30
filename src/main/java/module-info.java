module com.example.trabajofinal_interfaces {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires annotations;

    opens com.example.trabajofinal_interfaces to javafx.fxml;
    opens com.example.trabajofinal_interfaces.controlador to javafx.fxml;
    opens com.example.trabajofinal_interfaces.vista to javafx.fxml;
    exports com.example.trabajofinal_interfaces;
    exports com.example.trabajofinal_interfaces.vista;
    exports com.example.trabajofinal_interfaces.controlador;
    opens com.example.trabajofinal_interfaces.modelo to javafx.fxml;
    exports com.example.trabajofinal_interfaces.modelo;
    exports com.example.trabajofinal_interfaces.utiles;
    opens com.example.trabajofinal_interfaces.utiles to javafx.fxml;


}