package com.example.trabajofinal_interfaces.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class ControladorVentanaEleccion {

    private int id;

    @FXML
    private Button btnEventoGratis;

    @FXML
    private Button btnEventoPago;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @FXML
    void registroEventoGratis(ActionEvent event) {

    }

    @FXML
    void registroEventoPago(ActionEvent event) {

    }

}

