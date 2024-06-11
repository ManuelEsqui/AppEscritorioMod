//Manuel Esquivel Sevillano 2ºDAM
package com.example.trabajofinal_interfaces.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ControladorVentanaEleccion {

    @FXML
    private Button btnEventoGratis;

    @FXML
    private Button btnEventoPago;
    private String usu;

    //cambia de ventana a la de insertar evento gratis
    @FXML
    void registroEventoGratis(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaRegistroEventoGratis.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnEventoGratis.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaRegistroEventoGratis c = loader.getController();
        c.setUsu(usu);
        c.init();
        stage.close();
        stage.show();
    }

    //cambia de ventana a la de insertar evento de pago
    @FXML
    void registroEventoPago(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaRegistroEventoPago.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnEventoPago.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaRegistroEventoPago c = loader.getController();
        c.setUsu(usu);
        c.init();
        stage.close();
        stage.show();
    }

    public void setUsu(String usu) {
        this.usu=usu;
    }
}

