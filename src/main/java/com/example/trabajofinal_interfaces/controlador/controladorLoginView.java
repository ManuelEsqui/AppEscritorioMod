package com.example.trabajofinal_interfaces.controlador;

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

import java.io.IOException;
import java.sql.*;

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
            // Cargar el driver
            Class.forName(utiles.driver);

            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

            // Preparamos la consulta
            Statement sentencia = (Statement) conexion.createStatement();
            String sql = "SELECT user,passwrd,admin FROM personas;";
            ResultSet resul = sentencia.executeQuery(sql);

            // Recorremos el resultado para visualizar cada fila
            // Se hace un bucle mientras haya registros
            usuario=txtUsuario.getText();
            contra=txtContrasenia.getText();
            boolean bandera=false;
            while (resul.next()) {
                if (usuario.equals(resul.getString(1)) && contra.equals(resul.getString(2))){
                    if (resul.getBoolean(3)){
                        cambiarVentanaAdmin();
                        bandera=true;
                    }else{
                        cambiarVentanaUsuario();
                        bandera=true;
                    }
                }
            }if (!bandera){
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Incorrecto");
                alerta.setHeaderText(null);
                alerta.setContentText("El usuario o la contraseña son incorrectos");
                alerta.showAndWait();
            }

            resul.close(); // Cerrar ResultSet
            sentencia.close(); // Cerrar Statement
            conexion.close(); // Cerrar conexi�n

        } catch (ClassNotFoundException cn) {
            cn.printStackTrace();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarVentanaUsuario() throws IOException {
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

    private void cambiarVentanaAdmin() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAdmin.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) LogIn.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.show();
    }

    public void setStage(Stage stage) {
        this.stage=stage;
    }


    public void inicioSesionTeclado(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            inicioSesion();
        }
    }

    public void ventanaEditarUsuarios(MouseEvent mouseEvent) {

    }
}

