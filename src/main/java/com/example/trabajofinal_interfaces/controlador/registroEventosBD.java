package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class registroEventosBD {



    private void insertarEventoDePago(int id) throws ClassNotFoundException, SQLException {
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String sql = "INSERT INTO eventos (nombre, descripcion, fecha, localidad_id, ubicacion) VALUES (?, ?, ?, ?, ?);";
        PreparedStatement sentencia= conexion.prepareStatement(sql);
    }



    static int cogerIdEvento() throws IOException, ClassNotFoundException, SQLException {
        int id=-1;
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT id FROM eventos order by id desc LIMIT 1;";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            id = resul.getInt("id");
        }
        return id;
    }

}
