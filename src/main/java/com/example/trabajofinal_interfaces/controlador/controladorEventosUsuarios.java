package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class controladorEventosUsuarios {

    public Button desapuntarse;
    @FXML
    private TableColumn<Evento, String> ColumnaNombre;

    @FXML
    private TableColumn<Evento, String> columnaDescripcion;

    @FXML
    private TableColumn<Evento, Date> columnaFecha;

    @FXML
    private TableColumn<Evento, String> columnaLocalidad;

    @FXML
    private TableColumn<Evento, String> columnaUbicacion;

    @FXML
    private TableView<Evento> tablaEventos;
    private Evento evento;

    @FXML
    private Button volver;
    String usu;
    String contra;
    private ObservableList lista;
    public void setContra(String contra) {
        this.contra = contra;
    }

    public void setUsu(String usu) throws SQLException, ClassNotFoundException {
        this.usu = usu;
        inicializarTableView();
        this.lista=listAll();
        this.tablaEventos.setItems(lista);
    }

    private ObservableList listAll() throws SQLException, ClassNotFoundException {
        ObservableList<Evento> observableList= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT e.nombre, e.descripcion, e.fecha,e.ubicacion,l.nombre FROM eventos e INNER JOIN localidades l ON e.localidad_id = l.id INNER JOIN Persona_Evento pe ON e.id = pe.id_Evento INNER JOIN personas p ON pe.id_Persona = p.id WHERE p.user = '"+usu+"';";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            String nomb=resul.getNString(1);
            String descripcion=resul.getNString(2);
            Date fecha=resul.getDate(3);
            String ubicacion=resul.getNString(4);
            String loc_nombre=resul.getNString(5);
            observableList.add(new Evento(nomb,descripcion,loc_nombre,ubicacion,fecha));
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return observableList;
    }


    private void inicializarTableView() {
        this.ColumnaNombre.setCellValueFactory(new PropertyValueFactory<Evento, String>("nombre"));
        this.columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Evento, String>("descripcion"));
        this.columnaUbicacion.setCellValueFactory(new PropertyValueFactory<Evento, String>("ubicacion"));
        this.columnaLocalidad.setCellValueFactory(new PropertyValueFactory<Evento,String>("localidad"));
        this.columnaFecha.setCellValueFactory(new PropertyValueFactory<Evento, Date>("fecha"));
    }

    @FXML
    void volverVentanaUsuarios(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) volver.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaUsuarios c = (controladorVentanaUsuarios) loader.getController();
        c.setUsu(usu);
        c.setContra(contra);
        stage.close();
        stage.show();
    }

    public void delete(ActionEvent actionEvent) throws ClassNotFoundException, SQLException {
        evento=tablaEventos.getSelectionModel().getSelectedItem();
        if (evento!=null) {
            // Cargar el driver
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            Statement sentencia=(Statement) conexion.createStatement();
            String sql="DELETE pe FROM persona_evento pe INNER JOIN personas p ON pe.id_Persona = p.id INNER JOIN eventos e ON pe.id_Evento=e.id WHERE p.user like '"+usu+"' AND e.nombre like '"+evento.getNombre()+"';";
            sentencia.executeUpdate(sql);
            this.lista=listAll();
            this.tablaEventos.setItems(lista);
            Alert alerta=new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Eliminado");
            alerta.setHeaderText(null);
            alerta.setContentText("Has sido eliminado del evento seleccionado");
            alerta.showAndWait();

        }else{
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes de seleccionar un evento para borrarte");
            alerta.showAndWait();
        }

    }
}

