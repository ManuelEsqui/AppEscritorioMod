package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static com.example.trabajofinal_interfaces.controlador.registroEventosBD.cogerIdEvento;
import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaRegistroEventoPago {

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
    utiles u=new utiles();


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
    private void insertarEventoPago(int id) throws ClassNotFoundException, SQLException {
        float precio;
        try {
            precio = Float.parseFloat(txtPrecio.getText());
        }catch (Exception e) {
            Alertas(Alert.AlertType.ERROR, "Debe ser un numero", "El campo Precio es invalido");
            return;
        }
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String sql = "INSERT INTO eventosdepago (id, precio, venta_entrada) VALUES (?, ?, ?);";
        PreparedStatement sentencia= conexion.prepareStatement(sql);
        sentencia.setInt(1, id);
        sentencia.setFloat(2,precio);
        sentencia.setString(3,txtPuntoVenta.getText());
        sentencia.executeUpdate();
        Alertas(Alert.AlertType.INFORMATION,"Evento introducido","El evento se ha introducido correctamente");
        sentencia.close();
        conexion.close();
    }
    public void insertarEvento() throws SQLException, ClassNotFoundException, IOException {

        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String ubicacion = txtUbicación.getText();
        String localidad = cbLocalidades.getSelectionModel().getSelectedItem();
        LocalDate datelocal = dpFecha.getValue();
        boolean bandera;
        if (nombre.length()<1 || descripcion.length()<1 || ubicacion.length()<1 || localidad.length()<1 || datelocal==null || txtPuntoVenta.getText().length()<1){
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
            if (!encontrada){
                Alertas(Alert.AlertType.ERROR, "Error", "Localidad no encontrada en la base de datos, revise si existe y si no es el caso agréguela");
                return;
            }
            //Se hace la consulta para añadir el evento a la bd
            String sql = "INSERT INTO eventos (nombre, descripcion, fecha, localidad_id, ubicacion) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2,descripcion);
            sentencia.setDate(3,date);
            sentencia.setInt(4, id_loc);
            sentencia.setString(5,ubicacion);
            sentencia.executeUpdate();
            sentencia.close();
            resul.close();
            int id=cogerIdEvento();
            insertarEventoPago(id);
        }else{
            Alertas(Alert.AlertType.ERROR, "No se pudo introducir", "Se deben rellenar todos los campos para insertar el evento correctamente");
        }
    }

    public void ventanaEditarEventos(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        u.cambiarVentanaAdminEventos((Stage) txtUbicación.getScene().getWindow());
    }

    public void ventanaLocalidades(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        u.cambiarVentanaLocalidades((Stage) cbLocalidades.getScene().getWindow());
    }

    public void ventanaVistaEventos(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        u.CambiarVistaEventosDesdeAdmin((Stage) txtUbicación.getScene().getWindow());
    }

    public void init() throws SQLException, ClassNotFoundException {
        inicializarComboBox();
    }
}
