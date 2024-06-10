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
import java.util.ArrayList;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class controladorGestorPersonas {

    // Elementos de la interfaz de usuario
    public CheckBox checkAdmin; // Casilla de verificación para administrador

    @FXML
    private Button btnVolver; // Botón para volver

    @FXML
    private TextField txtApellidos; // Campo de texto para los apellidos

    @FXML
    private TextField txtContrasenia; // Campo de texto para la contraseña

    @FXML
    private TextField txtEdad; // Campo de texto para la edad

    @FXML
    private TextField txtEstadoCivil; // Campo de texto para el estado civil

    @FXML
    private ComboBox<String> cbLocalidades; // Menú desplegable para las localidades

    @FXML
    private TextField txtNombre; // Campo de texto para el nombre

    @FXML
    private ComboBox<String> cbSexo; // Menú desplegable para el sexo

    @FXML
    private TextField txtUsuario; // Campo de texto para el nombre

    // Variables de control
    private String nombre, apellidos, sexo, estadoCivil, user, passwrd, localidad; // Datos del usuario
    private int edad; // Edad del usuario
    private String usu; // Usuario actual
    boolean bandera = true; // Bandera de control

    // Método para establecer el usuario
    public void setUsu(String usu) {
        this.usu = usu;
    }

    // Método para activar la casilla de verificación de administrador
    private void activarCheck() {
        checkAdmin.setDisable(false);
    }

    // Método para registrar un nuevo usuario
    @FXML
    void registrarse(ActionEvent event) {
        try {
            // Conexión a la base de datos
            Class.forName(utiles.driver);
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

            // Recolección de datos del formulario
            nombre = this.txtNombre.getText();
            apellidos = this.txtApellidos.getText();
            sexo = this.cbSexo.getValue();
            estadoCivil = this.txtEstadoCivil.getText();
            user = this.txtUsuario.getText();
            passwrd = this.txtContrasenia.getText();

            // Validación de la contraseña
            String regex = "^.{8,}$";
            if (!passwrd.matches(regex)) {
                Alertas(Alert.AlertType.WARNING, "Warning", "La contraseña no es segura");
                return;
            }

            // Obtención de la localidad
            localidad = cbLocalidades.getValue();

            // Obtención de la edad
            String sEdad = txtEdad.getText();
            try {
                edad = Integer.parseInt(sEdad);
            } catch (Exception e) {
                Alertas(Alert.AlertType.ERROR, "Fallo", "Debes de rellenar el campo edad con un número");
                return;
            }

            int id_loc = 6;
            boolean comprobador;
            if (user.length() < 1 || passwrd.length() < 1 || nombre.length() < 1 || sexo == null || estadoCivil.length() < 1 || apellidos.length() < 1 || localidad == null) {
                comprobador = false;
            } else {
                comprobador = true;
            }

            // Verificación de campos requeridos
            if (comprobador) {
                Statement sentencia2 = (Statement) conexion.createStatement();
                String sql2 = "SELECT * FROM Localidades;";
                ResultSet resul = sentencia2.executeQuery(sql2);

                // Recorrido para obtener el ID de la localidad
                while (resul.next()) {
                    if (resul.getString(2).equalsIgnoreCase(localidad)) {
                        id_loc = resul.getInt(1);
                    }
                }

                // Verificación de usuario existente
                sql2 = "SELECT user FROM personas;";
                resul = sentencia2.executeQuery(sql2);
                while (resul.next()) {
                    if (resul.getString(1).equals(user)) {
                        Alertas(Alert.AlertType.ERROR, "Usuario existente", "Prueba con otro nombre de usuario");
                        return;
                    }
                }

                // Inserción de los datos en la base de datos
                String sql = "INSERT INTO personas (nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, admin, localidad_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                sentencia.setString(2, apellidos);
                sentencia.setString(3, sexo);
                sentencia.setString(4, estadoCivil);
                sentencia.setString(5, user);
                sentencia.setString(6, passwrd);
                sentencia.setInt(7, edad);
                sentencia.setBoolean(8, checkAdmin.isSelected());
                sentencia.setInt(9, id_loc);
                sentencia.executeUpdate();
                Alertas(Alert.AlertType.INFORMATION, "Añadido", "Persona añadida con éxito");
                sentencia.close();
                sentencia2.close();
                resul.close();
                volver();
            } else {
                Alertas(Alert.AlertType.ERROR, "Error", "Se deben rellenar todos los datos");
            }
            conexion.close();

        } catch (ClassNotFoundException | SQLException | IOException e) {
            Alertas(Alert.AlertType.ERROR, "Error", "Ha ocurrido un error inesperado");
        }
    }

    // Método para volver a la ventana principal
    @FXML
    void volver() throws IOException, SQLException, ClassNotFoundException {
        if (bandera) {
            new utiles().cambiarVentanaLogin((Stage) btnVolver.getScene().getWindow());
        } else {
            new utiles().cambiarVentanaAdmin((Stage) btnVolver.getScene().getWindow(), usu);
        }
    }

    // Método para inicializar los ComboBox
    public void inicializarComboBox(boolean bandera) throws SQLException {
        ObservableList<String> tiposSexo = FXCollections.observableArrayList();
        tiposSexo.addAll("Hombre", "Mujer", "Otro");
        cbSexo.setItems(tiposSexo);
        this.bandera = bandera;
        if (!bandera) {
            activarCheck();
        }
        ArrayList<String> localidades = new ArrayList<>();
        Connection cn = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM localidades;");
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            localidades.add(rs.getString(1));
        }
        ObservableList<String> observableListLocalidades = FXCollections.observableArrayList();
        observableListLocalidades.addAll(localidades);
        cbLocalidades.setItems(observableListLocalidades);
    }

}