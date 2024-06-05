package com.example.trabajofinal_interfaces.utiles;

import com.example.trabajofinal_interfaces.controlador.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class utiles {

    public void cambiarVentanaAdminEventos(Stage stage, String usu) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorEventos.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorGestorEventos c= loader.getController();
        c.setUsu(usu);
        c.init("");
        stage.setResizable(false);
        stage.close();
        stage.show();
    }

    public void cambiarVentanaAdmin(Stage stage, String usu) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAdmin.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorVentanaAdmin c = loader.getController();
        c.setUsu(usu);
        c.init();
        stage.close();
        stage.setResizable(false);
        stage.show();
    }
    public void cambiarVentanaLogin(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
        Scene escena = new Scene(root);
        stage.setScene(escena);
        stage.close();
        stage.setResizable(false);
        stage.show();
    }
    public void cambiarVentanaLocalidades(Stage stage, String usu) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaGestionLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorVentanaGestionLocalidades c = loader.getController();
        c.setUsu(usu);
        c.inicializarLocalidades();
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
    public void CambiarVistaEventosDesdeAdmin(Stage stage, String usu) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorVentanaUsuarios c=loader.getController();
        c.setUsuAdmin(usu);
        c.init();
        stage.close();
        stage.show();
    }

    public void cambiarVentanaAddEvento(Stage stage, String usu) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaEleccion.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        ControladorVentanaEleccion c=loader.getController();
        c.setUsu(usu);
        stage.close();
        stage.show();
    }
    public void ventanaSelecEventos(Stage stage, String usu) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        stage.setScene(escena);
        controladorVentanaUsuarios c = loader.getController();
        c.setUsu(usu);
        c.init();
        stage.show();
    }

    public static String url="jdbc:mysql://localhost/extreventos";
    public static String usuario="root";
    public static String clave="";
    public static String driver="com.mysql.cj.jdbc.Driver";
}//com.mysql.jdbc.Driver
