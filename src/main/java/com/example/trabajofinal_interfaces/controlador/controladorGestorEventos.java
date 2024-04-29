package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento;
import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;

public class controladorGestorEventos {

    @FXML
    private DatePicker AddFecha;

    @FXML
    private Button BtnVolver;

    @FXML
    private TableColumn<Evento, Integer> ColumnaId;

    @FXML
    private TableColumn<Evento, String> ColumnaNombre;

    @FXML
    private DatePicker EditFecha;

    @FXML
    private TableView<Evento> TableViewEventos;
    private ObservableList lista;

    @FXML
    private Label lbError;

    @FXML
    private TextField txtAddLocalidad;

    @FXML
    private TextField txtAddNombre;

    @FXML
    private TextField txtAddUbicacion;

    @FXML
    private TextField txtBuscar;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextArea txtEditDescripcion;

    @FXML
    private TextField txtEditLocalidad;

    @FXML
    private TextField txtEditNombre;

    @FXML
    private TextField txtEditUbicacion;

    @FXML
    private TextField txtId;
    int idEvento;
    private Evento eventoSelec;

    @FXML
    void AddEvento(ActionEvent event) throws ClassNotFoundException, SQLException, ParseException {//metodo para añadir  un evento
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        String nombre = txtAddNombre.getText();
        String descripcion = txtDescripcion.getText();
        String ubicacion = txtAddUbicacion.getText();
        String localidad = txtAddLocalidad.getText();
        LocalDate datelocal = AddFecha.getValue();
        boolean bandera;
        if (nombre.length()<1 || descripcion.length()<1 || ubicacion.length()<1 || localidad.length()<1 || datelocal==null){
            bandera=false;
        }else {
            bandera=true;
        }
        if (bandera) {
            Date date = Date.valueOf(datelocal);
            int id_loc=6;
            Statement sentencia2 = (Statement) conexion.createStatement();
            String sql2 = "SELECT * FROM Localidades;";
            ResultSet resul = sentencia2.executeQuery(sql2);

            // Recorremos el resultado para visualizar cada fila
            // Se hace un bucle mientras haya registros
            boolean encontrada=false;
            while (resul.next()) {

                if (resul.getString(2).equalsIgnoreCase(localidad)){
                    id_loc=resul.getInt(1);
                    encontrada=true;
                }

            }
            if (!encontrada) lbError.setText("Localidad no encontrada en la bd");
            //Se hace la consulta para añadir el evento a la bd
            String sql = "INSERT INTO eventos (nombre, descripcion, fecha, localidad_id, ubicacion) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2,descripcion);
            sentencia.setDate(3,date);
            sentencia.setInt(4, id_loc);
            sentencia.setString(5,ubicacion);
            sentencia.executeUpdate();
            Alert alerta=new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Introducido");
            alerta.setHeaderText(null);
            alerta.setContentText("Evento "+nombre+" introducido correctamente");
            alerta.showAndWait();
        }else{
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Se deben rellenar todos los datos");
            alerta.showAndWait();
        }


    }
    @FXML
    void DeleteEvento(ActionEvent event) throws SQLException, ClassNotFoundException {//metodo para eliminar el evento
        eventoSelec=TableViewEventos.getSelectionModel().getSelectedItem();
        txtId.setText(""+eventoSelec.getId());
        String sid=txtId.getText();

        if (!(sid.length()<1)){
            idEvento=Integer.parseInt(sid);
            Class.forName(utiles.driver);

            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            String sql = "DELETE FROM eventos WHERE id = ?;";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setInt(1, idEvento);
            int filas=sentencia.executeUpdate();
            System.out.println(filas+" rows afected");
            if (filas<1){
                lbError.setText("No se ha eliminado ningun evento");
            }else{
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Eliminado");
                alerta.setHeaderText(null);
                alerta.setContentText("Evento eliminado");
                alerta.showAndWait();
            }


            conexion.close();
            sentencia.close();
        }else {
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes introducir id para eliminar");
            alerta.showAndWait();
        }

    }

    @FXML
    void Editar(ActionEvent event) throws ClassNotFoundException, SQLException {//Metodo para editar el evento

        String nombre=txtEditNombre.getText();
        String idString=txtId.getText();
        if (nombre.length()<1 || idString.length()<1){
            rellenarCampos();
            return;
        }
        String descripcion=txtEditDescripcion.getText();
        String ubicacion=txtEditUbicacion.getText();
        String localidad=txtEditLocalidad.getText();
        LocalDate datelocal = EditFecha.getValue();
        int id_loc=6;
        boolean bandera;
        if (nombre.length()<1 || idString.length()<1 || descripcion.length()<1 || ubicacion.length()<1 || localidad.length()<1 || datelocal==null){
            bandera=false;
        }else {
            bandera=true;
        }
        if (bandera){
            Date date = Date.valueOf(datelocal);
            int id=Integer.parseInt(idString);
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

            Statement sentencia2 = (Statement) conexion.createStatement();
            String sql2 = "SELECT * FROM Localidades;";
            ResultSet resul = sentencia2.executeQuery(sql2);

            // Recorremos el resultado para visualizar cada fila
            // Se hace un bucle mientras haya registros
            boolean encontrada=false;
            while (resul.next()) {

                if (resul.getString(2).equalsIgnoreCase(localidad)){
                    id_loc=resul.getInt(1);
                    encontrada=true;
                }

            }
            if (!encontrada) lbError.setText("Localidad no encontrada en la bd");
            String sql = "UPDATE eventos SET nombre = ?, descripcion = ?, fecha = ?, localidad_id = ?, ubicacion = ? WHERE id = ?;";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            sentencia.setDate(3,date);
            sentencia.setInt(4, id_loc);
            sentencia.setString(5,ubicacion);
            sentencia.setInt(6,id);
            sentencia.executeUpdate();
            Alert alerta=new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Actualizado");
            alerta.setHeaderText(null);
            alerta.setContentText("Evento actualizado con exito");
            alerta.showAndWait();


            sentencia.close();
            sentencia2.close();
            resul.close();
        }else {
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Se deben rellenar todos los datos");
            alerta.showAndWait();
        }

    }

    private void rellenarCampos() {
        String nombre;
        String idString;
        eventoSelec=TableViewEventos.getSelectionModel().getSelectedItem();
        txtId.setText(""+eventoSelec.getId());
        txtEditNombre.setText(eventoSelec.getNombre());
        nombre=txtEditNombre.getText();
        idString=txtId.getText();
    }

    @FXML
    void buscarEvento(ActionEvent event) throws SQLException, ClassNotFoundException {
        String nombreEvento=txtBuscar.getText();
        inicializarTableView();
        this.lista=listAll(nombreEvento);
        this.TableViewEventos.setItems(lista);
    }

    private ObservableList listAll(String nombreEvento) throws SQLException, ClassNotFoundException {
        ObservableList<Evento> listUser= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2="SELECT id,nombre from eventos;";
        if (nombreEvento.length()>1){
            sql2 = "SELECT id,nombre from eventos WHERE nombre = '"+nombreEvento+"';";
        }
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            int id=resul.getInt(1);
            String nomb=resul.getNString(2);
            listUser.add(new Evento(id,nomb));
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return listUser;
    }

    private void inicializarTableView() {
        this.ColumnaId.setCellValueFactory(new PropertyValueFactory<Evento, Integer>("id"));
        this.ColumnaNombre.setCellValueFactory(new PropertyValueFactory<Evento, String>("nombre"));
    }

    @FXML
    void VentanaLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));

        Scene escena = new Scene(root);
        Stage stage =(Stage) BtnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    @FXML
    void VentanaPersonas(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAdmin.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) BtnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    @FXML
    void VentanaUsuarios(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) BtnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

}

