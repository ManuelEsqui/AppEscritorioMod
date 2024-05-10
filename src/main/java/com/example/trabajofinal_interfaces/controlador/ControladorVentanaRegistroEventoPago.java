package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

public class ControladorVentanaRegistroEventoPago {
    private int id;

    @FXML
    private ComboBox<String> cbLocalidades;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtPuntoVenta;

    @FXML
    private TextField txtUbicación;

    public void setId(int id) throws SQLException, ClassNotFoundException {
        this.id = id;
        inicializarComboBox();
    }
    public void inicializarComboBox() throws SQLException, ClassNotFoundException {
        ObservableList<String> listaLocalidades = FXCollections.observableArrayList();
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        Statement sentencia2 = conexion.createStatement();
        String sql2 = "SELECT nombre FROM Localidades;";
        ResultSet resul = sentencia2.executeQuery(sql2);

        // Recorremos el resultado para visualizar cada fila
        // Se hace un bucle mientras haya registros
        while (resul.next()) {
            listaLocalidades.add(resul.getString("nombre"));
            }
        cbLocalidades.setItems(listaLocalidades);
    }
}
