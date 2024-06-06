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
import java.util.ArrayList;
import java.util.Optional;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaEdicionUsuarios {

    @FXML
    private ComboBox<String> cbSexo;

    @FXML
    private CheckBox checkAdmin;


    @FXML
    private TextField txtApellidos;

    @FXML
    private PasswordField txtContrasenia;

    @FXML
    private TextField txtEdad;

    @FXML
    private TextField txtEstadoCivil;

    @FXML
    private ComboBox<String> cbLocalidades;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtUsuario;
    private Usuario usuario;
    private boolean bandera=false;
    private String usuAdmin;

    public void setBandera(boolean bandera) {
        this.bandera = bandera;
        if(this.bandera){
            checkAdmin.setSelected(false);
        }
    }

    public void setUsuario(Usuario usuario) throws SQLException {
        this.usuario = usuario;
        rellenarCampos();
        if (bandera){
            activarCheck();
        }
        inicializarComboBox();
    }

    private void activarCheck() {
        checkAdmin.setDisable(false);
    }

    public void inicializarComboBox() throws SQLException {
        ObservableList<String> tiposSexo = FXCollections.observableArrayList();
        tiposSexo.addAll("Hombre","Mujer","Otro");
        cbSexo.setItems(tiposSexo);
        ArrayList<String> localidades=new ArrayList<>();
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

    private void rellenarCampos() {
        if(usuario!=null){
            txtNombre.setText(usuario.getNombre());
            txtApellidos.setText(usuario.getApellidos());
            txtEdad.setText(usuario.getEdad()+"");
            cbSexo.setValue(usuario.getSexo());
            txtEstadoCivil.setText(usuario.getEstadoCivil());
            txtUsuario.setText(usuario.getUser());
            txtContrasenia.setText(usuario.getPasswrd());
            cbLocalidades.setValue(usuario.getLocalidad());
        }
    }

    @FXML
    void editarDatos(ActionEvent event) {//metodo para editar datos de los usuarios
        if (usuario==null){
            Alertas(Alert.AlertType.ERROR, "Error", "No has seleccinado ningun usuario");
            return;
        }
        try {
            // Cargar el driver
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            //Se recogen los datos
            String nombre=this.txtNombre.getText();
            String apellidos=this.txtApellidos.getText();
            String sexo=this.cbSexo.getValue();
            String estadoCivil=this.txtEstadoCivil.getText();
            String user=this.txtUsuario.getText();
            String passwrd=this.txtContrasenia.getText();
            String regex="^.{8,}$";
            if (!passwrd.matches(regex)){
                Alertas(Alert.AlertType.WARNING, "Warning", "La contraseña no es segura");
                return;
            }
            String localidad=cbLocalidades.getValue();
            String sEdad=txtEdad.getText();
            int edad;
            try {
                edad=Integer.parseInt(sEdad);
            }catch (Exception e){
                //Crea una alerta con un aviso
                Alertas(Alert.AlertType.ERROR, "Fallo", "Debes de rellenar el campo edad con un numero");
                return;
            }
            int id_loc=6;
            boolean comprobador;
            if (user.length()<1 || passwrd.length()<1 || nombre.length()<1 || sexo.length()<1 || estadoCivil.length()<1 || apellidos.length()<1 || localidad.length()<1){
                comprobador=false;
            }else{
                comprobador= true;
                if (checkAdmin.isSelected()){
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmar");
                    alert.setHeaderText("Hacer admin");
                    alert.setContentText("¿Estas seguro de que quieres hacer administrador\na este usuario");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() != ButtonType.OK) checkAdmin.setSelected(false);
                }
            }

            if (comprobador){
                Statement sentencia2 = conexion.createStatement();
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
                if (!encontrada) Alertas(Alert.AlertType.WARNING, "No se encontro la localidad", "Se te pondra la localidad desconocido por defecto");

                //Se crea la consulta para actualizar los datos
                String sql = "UPDATE Personas SET nombre = ?, apellidos = ?, sexo = ?, estadoCivil = ?, edad = ?,  localidad_id = ?, admin = ?, user = ?, passwrd = ? WHERE user = '"+usuario.getUser()+"' AND passwrd = '"+usuario.getPasswrd()+"';";
                PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
                sentencia.setString(1, nombre);
                sentencia.setString(2, apellidos);
                sentencia.setString(3, sexo);
                sentencia.setString(4, estadoCivil);
                sentencia.setInt(5,edad);
                sentencia.setInt(6,id_loc);
                sentencia.setBoolean(7, checkAdmin.isSelected());
                sentencia.setString(8,user);
                sentencia.setString(9,passwrd);
                sentencia.executeUpdate();
                Alertas(Alert.AlertType.INFORMATION, "Actualizado", "Cuenta actualizada correctamente");


                sentencia.close();
                sentencia2.close();
                resul.close();
                volver();
            }else{
                Alertas(Alert.AlertType.ERROR, "Error", "Se deben añadir todos los datos");
            }
            conexion.close();


        } catch (ClassNotFoundException | IOException | SQLException e) {
            Alertas(Alert.AlertType.ERROR, "Error", "Ha ocurrido un error inesperado al modificar el usuario");
        }


    }
    @FXML
    void eliminarCuenta(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {//metodo para eliminar usuarios
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Eliminar");
        alert.setHeaderText("Eliminar cuenta");
        alert.setContentText("¿Estas seguro de que quieres ser eliminado de la base\nde datos? La proxima vez que inicies sesión no podras\nacceder con esta cuenta");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() != ButtonType.OK) return;
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        //Se hace la consulta para eliminar segun el usuario y la contraseña
        String sql = "DELETE FROM Personas WHERE user like ? AND passwrd = ?;";
        PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
        //Se introducen los parametros en el preparedStatement
        sentencia.setString(1, usuario.getUser());
        sentencia.setString(2, usuario.getPasswrd());
        int filas=sentencia.executeUpdate();
        System.out.println(filas+" rows afected");
        if (filas<1){
            Alertas(Alert.AlertType.ERROR, "Fallo", "No ha sido posible eliminar la cuenta, puede deberse a que la cuenta ya no existe");
        }else{
            Alertas(Alert.AlertType.INFORMATION, "Tu cuenta se ha eliminado con éxito", "Has eliminado la cuenta de: "+usuario.getUser());
            volver();
        }


        conexion.close();
        sentencia.close();


    }

    @FXML
    void volver() throws IOException, SQLException, ClassNotFoundException {
        if(!bandera){
            new utiles().cambiarVentanaLogin((Stage) txtApellidos.getScene().getWindow());
        }else{
            new utiles().cambiarVentanaAdmin((Stage) txtApellidos.getScene().getWindow(), usuAdmin);
        }
    }

    public void setusuAdmin(String usu) {
        this.usuAdmin=usu;
        checkAdmin.setSelected(true);
    }
}
