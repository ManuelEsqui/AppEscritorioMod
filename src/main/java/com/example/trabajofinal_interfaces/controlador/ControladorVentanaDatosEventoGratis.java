package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento_Gratis;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaDatosEventoGratis {

    @FXML
    private TextArea txtDescripcion;

    @FXML
    private TextArea txtDescripcionAdicional;

    @FXML
    private TextField txtLocalidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtTipo;

    @FXML
    private TextField txtUbicacion;
    private Evento_Gratis evento;
    private String usu;

    @FXML
    void apuntarse(ActionEvent event) {
        int id_usuario=-1;
        try {
            if(usu==null){
                Alertas(Alert.AlertType.INFORMATION, "No es posible","Un adminsitrador no se puede apuntar a un evento");
                return;
            }
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            Statement sentencia3 = (Statement) conexion.createStatement();
            String sql3 = "SELECT id FROM personas WHERE user = '"+usu+"';";
            ResultSet resul2 = sentencia3.executeQuery(sql3);
            while (resul2.next()) {
                id_usuario=resul2.getInt(1);
            }
            String sql = "INSERT INTO Persona_Evento (id_Persona, id_Evento) VALUES (?, ?);";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setInt(2, evento.getId());
            sentencia.setInt(1, id_usuario);
            sentencia.executeUpdate();
            Alertas(Alert.AlertType.INFORMATION, "Has sido apuntado al evento", "Has sido apuntado al evento correctamente");
            volverAtras();
        }catch (Exception e){
            Alertas(Alert.AlertType.ERROR,"Error","Ya estas apuntado a este evento");
        }
    }

    @FXML
    void volverAtras() throws SQLException, IOException, ClassNotFoundException {
        utiles u=new utiles();
        u.ventanaSelecEventos((Stage) txtDescripcion.getScene().getWindow(),usu);
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    public void setEvento_gratis(Evento_Gratis eventoGratis) {
        this.evento = eventoGratis;
        init();
    }
    private void init() {
        txtDescripcion.setText(evento.getDescripcion());
        txtDescripcionAdicional.setText(evento.getDescripcionAdicional());
        txtLocalidad.setText(evento.getLocalidad());
        txtNombre.setText(evento.getNombre());
        txtTipo.setText(evento.getTipo());
        txtUbicacion.setText(evento.getUbicacion());
    }
}

