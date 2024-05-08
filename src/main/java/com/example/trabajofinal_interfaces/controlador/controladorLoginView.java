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
    void VentanaRegistro(ActionEvent event) throws IOException {

//        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
//        Scene escena = new Scene(root);
//        Stage stage = (Stage) SingUp.getScene().getWindow();
//        stage.setScene(escena);
//        stage.close();
//        stage.show();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) LogIn.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorPersonas c = (controladorGestorPersonas) loader.getController();
        c.inicializarComboBox();
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
                    if (result.resul().getBoolean(3)){
                        Stage stage =(Stage) LogIn.getScene().getWindow();
                        u.cambiarVentanaAdmin(stage);
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

    private record Consulta(Connection conexion, Statement sentencia, ResultSet resul) {
    }

    private void cambiarVentanaUsuario() throws IOException, SQLException, ClassNotFoundException {
      //  Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) LogIn.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaUsuarios c = (controladorVentanaUsuarios) loader.getController();
        c.setUsu(usuario);
        c.setContra(contra);
        stage.close();
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage=stage;
    }

    public void ventanaEditarUsuarios(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {
        if(txtUsuario.getText().length()<1 || txtContrasenia.getText().length()<1){
            Alertas(Alert.AlertType.WARNING, "Introduce los datos", "Debes rellenar los campos usuario y contraseña");
            return;
        }
        boolean bandera=false;
        String sql = "SELECT personas.nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, localidades.nombre, admin FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id;";
        Consulta result = getConsulta(sql);
        while (result.resul().next()) {
            if (usuario.equals(result.resul().getString(5)) && contra.equals(result.resul().getString(6))){
                user=new Usuario(result.resul().getString(1), result.resul().getString(2), result.resul().getString(3), result.resul().getString(4), result.resul().getString(5),result.resul().getString(8), result.resul().getString(6), result.resul().getInt(7));
                if (result.resul().getBoolean(9)){
                    bandera=true;
                }
            }
        }
        if (user==null){
            Alertas(Alert.AlertType.ERROR, "Editar usuarios falló", "Usuario o contraseña icorrectos");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaEdicionUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) txtUsuario.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaEdicionUsuarios c = (ControladorVentanaEdicionUsuarios) loader.getController();
        c.setBandera(bandera);
        c.setUsuario(user);
        stage.close();
        stage.show();
    }
    @FXML
    void si(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            inicioSesion();
        }
    }
}

