package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Localidad;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

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
    void delete(ActionEvent event) throws SQLException, ClassNotFoundException {
        Localidad localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
        if(localidad.getNombre().equalsIgnoreCase("desconocida")){
            Alertas(Alert.AlertType.ERROR, "NO", "Es imposible eliminar este registro");
            return;
        }
        // Cargar el driver
        Class.forName(utiles.driver);

        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        //Se hace la consulta para eliminar segun el usuario y la contraseña
        String sql = "DELETE FROM localidades WHERE nombre like ? AND provincia like ?;";
        PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
        //Se introducen los parametros en el preparedStatement
        sentencia.setString(1, localidad.getNombre());
        sentencia.setString(2, localidad.getProvincia());
        int filas=sentencia.executeUpdate();
        System.out.println(filas+" rows afected");
        if (filas<1){
            Alertas(Alert.AlertType.ERROR, "Fallo", "Ha ocurrido un error");
        }else{
            Alertas(Alert.AlertType.INFORMATION, "Localidad eliminada", "Has eliminado la localidad de: "+localidad.getNombre());
        }
        inicializarLocalidades();
    }

    @FXML
    void update(ActionEvent event) throws IOException, SQLException {
        Localidad localidad;
        try {
            localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
        }catch (Exception e){
            Alertas(Alert.AlertType.ERROR, "Error", "Debes seleccionar una localidad para editarla");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAgregarEditarLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) menuButton.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaAgregarEditarLocalidades c = loader.getController();
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
