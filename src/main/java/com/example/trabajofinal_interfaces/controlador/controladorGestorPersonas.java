package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class controladorGestorPersonas {

    public CheckBox checkAdmin;

    @FXML
    private Button btnVolver;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtContrasenia;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEstadoCivil;

    @FXML
    private TextField txtLocalidad;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<String> cbSexo;
    boolean bandera=true;

    @FXML
    private TextField txtUsuario;
    private String nombre,apellidos,sexo,estadoCivil,user,passwrd,localidad;
    private int edad;
    String usu;
    public void setUsu(String usu) {
        this.usu = usu;
    }

    private void activarCheck() {
        checkAdmin.setDisable(false);
    }
   @FXML
    void registrarse(ActionEvent event) {//metodo para introducir nuevos usuarios

        try {
            // Cargar el driver
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            //Se recogen los datos
            nombre=this.txtNombre.getText();
            apellidos=this.txtApellidos.getText();
            sexo=this.cbSexo.getValue();
            estadoCivil=this.txtEstadoCivil.getText();
            user=this.txtUsuario.getText();
            passwrd=this.txtContrasenia.getText();
            String regex="^.{8,}$";
            if (!passwrd.matches(regex)){
                Alertas(Alert.AlertType.WARNING, "Warning", "La contraseña no es segura");
                return;
            }
            localidad=txtLocalidad.getText();
            String sEdad=txtEdad.getText();
            try {
                edad=Integer.parseInt(sEdad);
            }catch (Exception e){
                Alertas(Alert.AlertType.ERROR, "Fallo", "Debes de rellenar el campo edad con un numero");
                return;
            }

            int id_loc=6;
            boolean comprobador;
            if (user.length()<1 || passwrd.length()<1 || nombre.length()<1 || sexo==null || estadoCivil.length()<1 || apellidos.length()<1 || localidad.length()<1){
                comprobador=false;
            }else{
                comprobador= true;
            }

            if (comprobador){
                Statement sentencia2 = (Statement) conexion.createStatement();
                String sql2 = "SELECT * FROM Localidades;";
                ResultSet resul = sentencia2.executeQuery(sql2);


                // Recorremos el resultado para visualizar cada fila
                // Se hace un bucle mientras haya registros
                boolean encontrada=false;
                while (resul.next()) {

                    if (resul.getString(2).equalsIgnoreCase(localidad)){
                        id_loc=resul.getInt(1);
                        encontrada=true;
                    }

                }
                sql2="SELECT user FROM personas;";
                resul = sentencia2.executeQuery(sql2);
                //if (!encontrada) lbError.setText("Localidad no encontrada en la bd");
                while (resul.next()) {

                    if (resul.getString(1).equals(user)){
                        Alertas(Alert.AlertType.ERROR, "Usuario existente", "Prueba con otro nombre de usuario");
                        return;
                    }

                }


                String sql = "INSERT INTO personas (nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, admin, localidad_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
                //Se introducen los parametros en el preparedStatement
                sentencia.setString(1, nombre);
                sentencia.setString(2, apellidos);
                sentencia.setString(3, sexo);
                sentencia.setString(4, estadoCivil);
                sentencia.setString(5,user);
                sentencia.setString(6, passwrd);
                sentencia.setInt(7,edad);
                sentencia.setBoolean(8, checkAdmin.isSelected());
                sentencia.setInt(9, id_loc);
                sentencia.executeUpdate();
                Alertas(Alert.AlertType.INFORMATION, "Añadido", "Persona añadida con exito");
                sentencia.close();
                sentencia2.close();
                resul.close();
                volver();
            }else{
                Alertas(Alert.AlertType.ERROR, "Error", "Se deben rellenar todos los datos");
            }
            conexion.close();


        } catch (ClassNotFoundException | SQLException | IOException e) {
            Alertas(Alert.AlertType.ERROR, "Error", "Ha ocurrido un error inesperado");
        }

    }

    @FXML
    void volver() throws IOException, SQLException, ClassNotFoundException {// metodo para volver a la ventana principal
        if(bandera){
            new utiles().cambiarVentanaLogin((Stage) btnVolver.getScene().getWindow());
        }else{
            new utiles().cambiarVentanaAdmin((Stage) btnVolver.getScene().getWindow(), usu);
        }

    }

    public void inicializarComboBox(boolean bandera) {
        ObservableList<String> tiposSexo = FXCollections.observableArrayList();
        tiposSexo.addAll("Hombre","Mujer","Otro");
        cbSexo.setItems(tiposSexo);
        this.bandera=bandera;
        if (!bandera){
            activarCheck();
        }
    }

}

