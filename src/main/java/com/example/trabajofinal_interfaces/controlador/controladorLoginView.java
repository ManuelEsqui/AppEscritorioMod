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
    private Stage stage;

    @FXML
    private Button LogIn;

    @FXML
    private Button SingUp;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtUsuario;
    private String usuario;
    private String contra;
    Usuario user;
    utiles u = new utiles();


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

    @FXML
    void VentanaUsuario(ActionEvent event) {
        inicioSesion();
    }

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
            }if (!bandera){
                Alertas(Alert.AlertType.ERROR, "Login incorrecto", "Usuario o contraseña icorrectos");
            }

            result.resul().close(); // Cerrar ResultSet
            result.sentencia().close(); // Cerrar Statement
            result.conexion().close(); // Cerrar conexi�n

        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarRegistrosAterioresFechaActual() throws ClassNotFoundException, SQLException {
        Class.forName(utiles.driver);
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia = (Statement) conexion.createStatement();
        sentencia.executeUpdate("DELETE FROM eventos WHERE fecha < CURDATE();");//cada vez que alguien se logea se eliminan los eventos pasados de fecha
        sentencia.close();
        conexion.close();
    }

    private @NotNull Consulta getConsulta(String sql) throws ClassNotFoundException, SQLException {
        // Cargar el driver
        Class.forName(utiles.driver);

        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        // Preparamos la consulta
        Statement sentencia = (Statement) conexion.createStatement();
        ResultSet resul = sentencia.executeQuery(sql);

        // Recorremos el resultado para visualizar cada fila
        // Se hace un bucle mientras haya registros
        usuario=txtUsuario.getText();
        contra=txtContrasenia.getText();
        Consulta result = new Consulta(conexion, sentencia, resul);
        return result;
    }

    public void salirDeApp(ActionEvent actionEvent) {
        exit(777);
    }

    private record Consulta(Connection conexion, Statement sentencia, ResultSet resul) {
    }

    private void cambiarVentanaUsuario() throws IOException, SQLException, ClassNotFoundException {
      //  Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
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

    public void setStage(Stage stage) {
        this.stage=stage;
    }

    @FXML
    void si(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            inicioSesion();
        }
    }
}

