package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Localidad;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;

import java.sql.*;
import java.util.ArrayList;

public class controladorVentanaGestionLocalidades {

    @FXML
    private ImageView imagen;

    @FXML
    private ListView<String> listViewLocalidades;
    ArrayList<Localidad> localidades;
    ArrayList<String> localidadesNombre;

    @FXML
    private MenuButton menuButton;

    @FXML
    private Label provincia;

    @FXML
    void add(ActionEvent event) {

    }

    @FXML
    void delete(ActionEvent event) {

    }

    @FXML
    void update(ActionEvent event) {

    }
    private void inicializarLocalidades() throws ClassNotFoundException, SQLException {
        localidades = new ArrayList<>();
        localidadesNombre = new ArrayList<>();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT * FROM Localidades;";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            //int cont=0;
            localidades.add(new Localidad(resul.getString(2), resul.getString(3)));
            //localidadesNombre.add(localidades.get(cont).getNombre());
            localidadesNombre.add(resul.getString(2));
        }
        listViewLocalidades.setItems(FXCollections.observableArrayList(localidadesNombre));
    }

    public void edit(ListView.EditEvent<String> stringEditEvent) {
        //Implementar codigo
    }
}
