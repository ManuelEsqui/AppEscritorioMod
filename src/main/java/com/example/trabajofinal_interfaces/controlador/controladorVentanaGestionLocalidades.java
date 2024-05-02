package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Localidad;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
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
    void add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAgregarEditarLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) menuButton.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaAgregarEditarLocalidades c = (ControladorVentanaAgregarEditarLocalidades) loader.getController();
        c.inicializar(true);
        stage.close();
        stage.show();
    }

    @FXML
    void delete(ActionEvent event) {
        Localidad localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
    }

    @FXML
    void update(ActionEvent event) throws IOException {
        Localidad localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAgregarEditarLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) menuButton.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaAgregarEditarLocalidades c = (ControladorVentanaAgregarEditarLocalidades) loader.getController();
        c.inicializar(false);
        c.rellenarCampos(localidad.getNombre(),localidad.getProvincia());
        stage.close();
        stage.show();
    }
    public void inicializarLocalidades() throws ClassNotFoundException, SQLException {
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

    public void selecionarCiudad(MouseEvent mouseEvent) {
        String nombre=listViewLocalidades.getSelectionModel().getSelectedItem();
        for (Localidad localidad : localidades) {
            if (localidad.getNombre().equals(nombre)) {
                provincia.setText("Esta en la provincia de: "+localidad.getProvincia());
            }
        }
    }
}
