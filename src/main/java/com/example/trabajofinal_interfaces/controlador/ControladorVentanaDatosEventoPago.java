package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento_Pago;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaDatosEventoPago {

    // Referencias a elementos de la interfaz gráfica
    public Label prueba;

    @FXML
    private Button apuntarse;

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextField txtLocalidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtPuntoVenta;

    @FXML
    private TextField txtUbicacion;

    @FXML
    private Button volverAtras;

    // Variables para almacenar información del evento y del usuario
    private Evento_Pago evento_pago;
    private int id_usuario;
    private String usu;

    // Setter para el usuario
    public void setUsu(String usu) {
        this.usu = usu;
    }

    // Setter para el evento de pago
    public void setEvento_pago(Evento_Pago evento_pago) {
        this.evento_pago = evento_pago;
        // Inicializar la visualización de los detalles del evento
        init();
    }

    // Método para inicializar los detalles del evento en la interfaz gráfica
    private void init() {
        txtDescripcion.setText(evento_pago.getDescripcion());
        txtLocalidad.setText(evento_pago.getLocalidad());
        txtNombre.setText(evento_pago.getNombre());
        txtPuntoVenta.setText(evento_pago.getPuntoDeVenta());
        txtUbicacion.setText(evento_pago.getUbicacion());
        txtPrecio.setText(evento_pago.getPrecio() + ""); // Convertir el precio a String
    }

    // Método para manejar el evento de apuntarse a un evento
    @FXML
    void apuntarse(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            // Verificar si el usuario es un administrador, ya que no puede apuntarse a un evento
            if (usu == null) {
                Alertas(Alert.AlertType.INFORMATION, "No es posible", "Un administrador no se puede apuntar a un evento");
                return;
            }
            // Cargar el driver
            Class.forName(utiles.driver);
            // Establecer la conexión con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            Statement sentencia3 = (Statement) conexion.createStatement();
            String sql3 = "SELECT id FROM personas WHERE user = '" + usu + "';";
            ResultSet resul2 = sentencia3.executeQuery(sql3);
            // Obtener el ID del usuario
            while (resul2.next()) {
                id_usuario = resul2.getInt(1);
            }
            // Insertar el usuario en el evento
            String sql = "INSERT INTO Persona_Evento (id_Persona, id_Evento) VALUES (?, ?);";
            PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setInt(2, evento_pago.getId());
            sentencia.setInt(1, id_usuario);
            sentencia.executeUpdate();
            // Mostrar un mensaje de éxito
            Alertas(Alert.AlertType.INFORMATION, "Has sido apuntado al evento", "Has sido apuntado al evento correctamente");
            // Volver atrás
            volverAtras();
        } catch (Exception e) {
            // Manejar errores en caso de que el usuario ya esté apuntado al evento
            Alertas(Alert.AlertType.ERROR, "Error", "Ya estás apuntado a este evento");
        }

    }

    // Método para volver atrás
    @FXML
    void volverAtras() throws IOException, SQLException, ClassNotFoundException {
        // Instanciar la clase de utilidades
        utiles u = new utiles();
        // Volver a la ventana de selección de eventos
        u.ventanaSelecEventos((Stage) prueba.getScene().getWindow(), usu);
    }
}


