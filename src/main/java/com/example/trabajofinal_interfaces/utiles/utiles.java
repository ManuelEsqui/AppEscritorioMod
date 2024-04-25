package com.example.trabajofinal_interfaces.utiles;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class utiles {

    public void cambiarVentanaAdminEventos(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorEventos.fxml"));
        Scene escena = new Scene(root);
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    public void cambiarVentanaAdmin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAdmin.fxml"));
        Scene escena = new Scene(root);
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    public static String url="jdbc:mysql://localhost/extreventos";
    public static String usuario="root";
    public static String clave="";
    public static String driver="com.mysql.cj.jdbc.Driver";
}//com.mysql.jdbc.Driver
