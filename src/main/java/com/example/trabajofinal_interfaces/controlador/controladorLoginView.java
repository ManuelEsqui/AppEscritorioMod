package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;
import static java.lang.System.exit;

public class controladorLoginView {
    private Stage stage; // Referencia al escenario de la aplicación

    // Elementos de la interfaz de usuario
    @FXML
    private Button LogIn; // Botón de inicio de sesión

    @FXML
    private Button SingUp; // Botón de registro

    @FXML
    private TextField txtContrasenia; // Campo de texto para la contraseña

    @FXML
    private TextField txtUsuario; // Campo de texto para el usuario

    private String usuario; // Nombre de usuario ingresado
    private String contra; // Contraseña ingresada
    Usuario user; // Objeto de usuario
    utiles u = new utiles(); // Utilidades

    // Método para manejar el evento de abrir la ventana de registro
    @FXML
    void VentanaRegistro(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) LogIn.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorPersonas c = loader.getController();
        c.inicializarComboBox(true);
        stage.close();
        stage.show();
    }

    // Método para manejar el evento de iniciar sesión
    @FXML
    void VentanaUsuario(ActionEvent event) {
        inicioSesion();
    }

    // Método para realizar el inicio de sesión
    private void inicioSesion() {
        try {
            String sql = "SELECT user,passwrd,admin FROM personas;";
            Consulta result = getConsulta(sql);
            boolean bandera=false;
            while (result.resul().next()) {
                if (usuario.equals(result.resul().getString(1)) && contra.equals(result.resul().getString(2))){
                    eliminarRegistrosAterioresFechaActual();
                    if (result.resul().getBoolean(3)){
                        Stage stage =(Stage) LogIn.getScene().getWindow();
                        u.cambiarVentanaAdmin(stage, usuario);
                        bandera=true;
                    }else{
                        cambiarVentanaUsuario();
                        bandera=true;
                    }
                }
            }
            if (!bandera){
                Alertas(Alert.AlertType.ERROR, "Login incorrecto", "Usuario o contraseña incorrectos");
            }

            result.resul().close(); // Cerrar ResultSet
            result.sentencia().close(); // Cerrar Statement
            result.conexion().close(); // Cerrar conexión

        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    // Método para eliminar registros anteriores a la fecha actual
    private void eliminarRegistrosAterioresFechaActual() throws ClassNotFoundException, SQLException {
        Class.forName(utiles.driver);
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia = (Statement) conexion.createStatement();
        sentencia.executeUpdate("DELETE FROM eventos WHERE fecha < CURDATE();"); // Se eliminan eventos pasados de fecha
        sentencia.close();
        conexion.close();
    }

    // Método para obtener una consulta a partir de una consulta SQL
    private @NotNull Consulta getConsulta(String sql) throws ClassNotFoundException, SQLException {
        // Cargar el driver
        Class.forName(utiles.driver);

        // Establecer la conexión con la base de datos
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        // Preparar la consulta
        Statement sentencia = (Statement) conexion.createStatement();
        ResultSet resul = sentencia.executeQuery(sql);

        // Recorrer el resultado para visualizar cada fila
        // Se hace un bucle mientras haya registros
        usuario=txtUsuario.getText();
        contra=txtContrasenia.getText();
        Consulta result = new Consulta(conexion, sentencia, resul);
        return result;
    }

    // Método para manejar el evento de salir de la aplicación
    public void salirDeApp(ActionEvent actionEvent) {
        exit(777);
    }

    // Clase para almacenar una consulta SQL
    private record Consulta(Connection conexion, Statement sentencia, ResultSet resul) {
    }

    // Método para cambiar a la ventana de usuario
    private void cambiarVentanaUsuario() throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) LogIn.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaUsuarios c = loader.getController();
        c.setUsu(usuario);
        c.setContra(contra);
        stage.close();
        stage.show();
    }

    // Método para establecer el escenario de la aplicación
    public void setStage(Stage stage) {
        this.stage=stage;
    }

    // Método para manejar el evento cuando se presiona la tecla Enter
    @FXML
    void si(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            inicioSesion();
        }
    }
}


