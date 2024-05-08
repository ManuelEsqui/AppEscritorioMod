package com.example.trabajofinal_interfaces.utiles;

import com.example.trabajofinal_interfaces.controlador.controladorLoginView;
import com.example.trabajofinal_interfaces.controlador.controladorVentanaAdmin;
import com.example.trabajofinal_interfaces.controlador.controladorVentanaGestionLocalidades;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class utiles {

    public void cambiarVentanaAdminEventos(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorEventos.fxml"));
        Scene escena = new Scene(root);
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    public void cambiarVentanaAdmin(Stage stage) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAdmin.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorVentanaAdmin c = loader.getController();
        c.init();
        stage.close();
        stage.show();
    }
    public void cambiarVentanaLogin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
        Scene escena = new Scene(root);
        stage.setScene(escena);
        stage.close();
        stage.show();
    }
    public static void Alertas(Alert.AlertType type, String Fallo, String s) {
        Alert alerta = new Alert(type);
        alerta.setTitle(Fallo);
        alerta.setHeaderText(null);
        alerta.setContentText(s);
        alerta.showAndWait();
    }

    public static String url="jdbc:mysql://localhost/extreventos";
    public static String usuario="root";
    public static String clave="";
    public static String driver="com.mysql.cj.jdbc.Driver";
}//com.mysql.jdbc.Driver
