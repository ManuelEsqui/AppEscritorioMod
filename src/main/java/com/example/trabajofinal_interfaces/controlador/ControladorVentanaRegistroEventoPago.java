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

    // Referencias a elementos de la interfaz gráfica
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

    // Instancia de la clase de utilidades
    utiles u = new utiles();
    String usu; // Variable para almacenar el usuario

    // Setter para el usuario
    public void setUsu(String usu) {
        this.usu = usu;
    }

    // Método para inicializar el ComboBox de localidades
    public void inicializarComboBox() throws SQLException, ClassNotFoundException {
        ObservableList<String> listaLocalidades = FXCollections.observableArrayList();
        Class.forName(utiles.driver);
        // Establecer la conexión con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        Statement sentencia2 = conexion.createStatement();
        String sql2 = "SELECT nombre FROM Localidades;";
        ResultSet resul = sentencia2.executeQuery(sql2);

        // Recorrer el resultado para agregar cada localidad al ComboBox
        while (resul.next()) {
            listaLocalidades.add(resul.getString("nombre"));
        }
        cbLocalidades.setItems(listaLocalidades);
    }

    // Método para insertar un evento de pago en la base de datos
    private void insertarEventoPago(int id) throws ClassNotFoundException, SQLException, IOException {
        float precio;
        try {
            precio = Float.parseFloat(txtPrecio.getText());
        } catch (Exception e) {
            // Mostrar un mensaje de error si el precio no es un número
            Alertas(Alert.AlertType.ERROR, "Debe ser un número", "El campo Precio es inválido");
            return;
        }
        Class.forName(utiles.driver);
        // Establecer la conexión con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String sql = "INSERT INTO eventosdepago (id, precio, venta_entrada) VALUES (?, ?, ?);";
        PreparedStatement sentencia = conexion.prepareStatement(sql);
        sentencia.setInt(1, id);
        sentencia.setFloat(2, precio);
        sentencia.setString(3, txtPuntoVenta.getText());
        sentencia.executeUpdate();
        // Mostrar un mensaje de éxito
        Alertas(Alert.AlertType.INFORMATION, "Evento introducido", "El evento se ha introducido correctamente");
        sentencia.close();
        conexion.close();
        // Cambiar a la ventana de edición de eventos
        ventanaEditarEventos();
    }

    // Método para insertar un evento en la base de datos
    public void insertarEvento() throws SQLException, ClassNotFoundException, IOException {
        Class.forName(utiles.driver);
        // Establecer la conexión con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String nombre = txtNombre.getText();
        String descripcion = txtDescripcion.getText();
        String ubicacion = txtUbicación.getText();
        String localidad = cbLocalidades.getSelectionModel().getSelectedItem();
        LocalDate datelocal = dpFecha.getValue();
        boolean bandera;
        // Verificar si todos los campos están completos
        if (nombre.length() < 1 || descripcion.length() < 1 || ubicacion.length() < 1 || localidad.length() < 1 || datelocal == null || txtPuntoVenta.getText().length() < 1) {
            bandera = false;
        } else {
            bandera = true;
            try{
                Float.parseFloat(txtPrecio.getText());
            }catch (Exception e){
                Alertas(Alert.AlertType.ERROR, "se debe introducir un numero", "campo precio tiene que lleva un numero");
                return;
            }
        }
        if (bandera) {
            Date date = Date.valueOf(datelocal);
            int id_loc = 6;
            Statement sentencia2 = (Statement) conexion.createStatement();
            String sql2 = "SELECT * FROM Localidades;";
            ResultSet resul = sentencia2.executeQuery(sql2);

            // Recorrer el resultado para encontrar el ID de la localidad seleccionada
            boolean encontrada = false;
            while (resul.next()) {
                if (resul.getString(2).equalsIgnoreCase(localidad)) {
                    id_loc = resul.getInt(1);
                    encontrada = true;
                }
            }
            // Verificar si la localidad fue encontrada en la base de datos
            if (!encontrada) {
                // Mostrar un mensaje de error si la localidad no está en la BD
                Alertas(Alert.AlertType.ERROR, "Error", "Localidad no encontrada en la base de datos, revise si existe y si no es el caso agréguela");
                return;
            }
            // Insertar el evento en la tabla eventos
            String sql = "INSERT INTO eventos (nombre, descripcion, fecha, localidad_id, ubicacion) VALUES (?, ?, ?, ?, ?);";
            PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            sentencia.setDate(3, date);
            sentencia.setInt(4, id_loc);
            sentencia.setString(5, ubicacion);
            sentencia.executeUpdate();
            sentencia.close();
            resul.close();
            int id = cogerIdEvento();
            // Insertar el evento de pago relacionado con el evento principal
            insertarEventoPago(id);
        } else {
            // Mostrar un mensaje de error si no todos los campos están completos
            Alertas(Alert.AlertType.ERROR, "No se pudo introducir", "Se deben rellenar todos los campos para insertar el evento correctamente");
        }
    }

    // Método para cambiar a la ventana de edición de eventos
    public void ventanaEditarEventos() throws SQLException, IOException, ClassNotFoundException {
        u.cambiarVentanaAdminEventos((Stage) txtUbicación.getScene().getWindow(), usu);
    }

    // Método para cambiar a la ventana de gestión de localidades
    public void ventanaLocalidades(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        u.cambiarVentanaLocalidades((Stage) cbLocalidades.getScene().getWindow(), usu);
    }

    // Método para cambiar a la ventana de visualización de eventos desde el administrador
    public void ventanaVistaEventos(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        u.CambiarVistaEventosDesdeAdmin((Stage) txtUbicación.getScene().getWindow(), usu);
    }

    public void init() throws SQLException, ClassNotFoundException {
        inicializarComboBox();
    }
}
