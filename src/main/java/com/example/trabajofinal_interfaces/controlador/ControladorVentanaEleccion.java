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

    private int id;

    @FXML
    private Button btnEventoGratis;

    @FXML
    private Button btnEventoPago;

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    void registroEventoGratis(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaRegistroEventoGratis.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnEventoGratis.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaRegistroEventoGratis c = loader.getController();
        c.setId(id);
        stage.close();
        stage.show();
    }

    @FXML
    void registroEventoPago(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaRegistroEventoPago.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnEventoPago.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaRegistroEventoPago c = loader.getController();
        c.setId(id);
        stage.close();
        stage.show();
    }

}

