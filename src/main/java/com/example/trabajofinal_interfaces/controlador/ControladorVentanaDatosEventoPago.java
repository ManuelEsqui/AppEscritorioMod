package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento_Pago;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaDatosEventoPago {

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
    private Evento_Pago evento_pago;
    private int id_usuario;
    private String usu;

    public String getUsu() {
        return usu;
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    public Evento_Pago getEvento_pago() {
        return evento_pago;
    }

    public void setEvento_pago(Evento_Pago evento_pago) {
        this.evento_pago = evento_pago;
        init();
    }

    private void init() {
        txtDescripcion.setText(evento_pago.getDescripcion());
        txtLocalidad.setText(evento_pago.getLocalidad());
        txtNombre.setText(evento_pago.getNombre());
        txtPuntoVenta.setText(evento_pago.getPuntoDeVenta());
        txtUbicacion.setText(evento_pago.getUbicacion());
        txtPrecio.setText(evento_pago.getPrecio()+"");
    }

    @FXML
    void apuntarse(ActionEvent event) throws ClassNotFoundException, SQLException {
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia3 = (Statement) conexion.createStatement();
        String sql3 = "SELECT id FROM personas WHERE user LIKE '"+usu+"';";
        ResultSet resul2 = sentencia3.executeQuery(sql3);
        while (resul2.next()) {
            id_usuario=resul2.getInt(1);
        }
        String sql = "INSERT INTO Persona_Evento (id_Persona, id_Evento) VALUES (?, ?);";
        PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
        sentencia.setInt(2, evento_pago.getId());
        sentencia.setInt(1, id_usuario);
        sentencia.executeUpdate();
        Alertas(Alert.AlertType.INFORMATION, "Has sido apuntado al evento", "Has sido apuntado al evento correctamente");
    }

    @FXML
    void volverAtras(ActionEvent event) {

    }

}
