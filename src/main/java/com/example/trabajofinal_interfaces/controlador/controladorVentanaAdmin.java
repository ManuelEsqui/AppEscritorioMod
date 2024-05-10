package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class controladorVentanaAdmin {

    @FXML
    private TableColumn<Usuario, String> ColumnaApellido;

    @FXML
    private TableColumn<Usuario, Integer> ColumnaEdad;

    @FXML
    private TableColumn<Usuario, String> ColumnaEstadoCivil;

    @FXML
    private TableColumn<Usuario, String> ColumnaLocalidad;

    @FXML
    private TableColumn<Usuario, String> ColumnaNombre;

    @FXML
    private TableColumn<Usuario, String> ColumnaPasswrd;

    @FXML
    private TableColumn<Usuario, String> ColumnaSexo;

    @FXML
    private TableColumn<Usuario, String> ColumnaUsuario;

    @FXML
    private Button btnVolver;

    @FXML
    private TableView<Usuario> tableViewUsuarios;

    private ObservableList lista;
    private Usuario usuarioselec;

    public void init() throws SQLException, ClassNotFoundException {//inicializa la tabla de los usuarios
        inicializarTableView();
        this.lista=listAll();
        this.tableViewUsuarios.setItems(lista);
    }

    @FXML
    void AdministrarEventos(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {//Cambia a la ventana de evenstos
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorEventos.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorEventos controladorGestorEventos = loader.getController();
        controladorGestorEventos.init("");
        stage.close();
        stage.show();
    }

    @FXML
    void AdministrarUsuarios(ActionEvent event) throws IOException {//Cambia a la ventana de editar usuarios
        usuarioselec=tableViewUsuarios.getSelectionModel().getSelectedItem();
        if(usuarioselec==null){
            Alertas(Alert.AlertType.ERROR, "Error", "No has seleccinado ningun usuario");
            return;
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaEdicionUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaEdicionUsuarios c = loader.getController();
        c.setBandera(true);
        c.setUsuario(usuarioselec);
        stage.close();
        stage.show();
    }


    @FXML
    void VolverLogin(ActionEvent event) throws IOException {
        new utiles().cambiarVentanaLogin((Stage) btnVolver.getScene().getWindow());
    }
    @FXML
    public void CargarDatos(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
//        inicializarTableView();
//        this.lista=listAll();
//        this.tableViewUsuarios.setItems(lista);
        init();
    }
    public ObservableList<Usuario> listAll() throws ClassNotFoundException, SQLException {
        ObservableList<Usuario> listUser= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = conexion.createStatement();
        String sql2 = "SELECT personas.nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, localidades.nombre FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id;";//Selecciona los datos de todas las personas
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {// Extraer los datos de la base de datos
            String nomb=resul.getNString(1);
            String apell=resul.getNString(2);
            String sex=resul.getNString(3);
            String estado=resul.getNString(4);
            String usu=resul.getNString(5);
            String pass= resul.getNString(6);
            int ed=resul.getInt(7);
            String locID=resul.getString(8);
            listUser.add(new Usuario(nomb,apell,sex,estado,usu,locID,pass,ed));//Añade las personas a una lista
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return listUser;
    }
    private void inicializarTableView() { // Añadir los campos a las columnas
        this.ColumnaNombre.setCellValueFactory(new PropertyValueFactory<Usuario, String>("nombre"));
        this.ColumnaApellido.setCellValueFactory(new PropertyValueFactory<Usuario, String>("apellidos"));
        this.ColumnaSexo.setCellValueFactory(new PropertyValueFactory<Usuario, String>("sexo"));
        this.ColumnaEstadoCivil.setCellValueFactory(new PropertyValueFactory<Usuario, String>("estadoCivil"));
        this.ColumnaUsuario.setCellValueFactory(new PropertyValueFactory<Usuario, String>("user"));
        this.ColumnaPasswrd.setCellValueFactory(new PropertyValueFactory<Usuario, String>("passwrd"));
        this.ColumnaEdad.setCellValueFactory(new PropertyValueFactory<Usuario, Integer>("edad"));
        this.ColumnaLocalidad.setCellValueFactory(new PropertyValueFactory<Usuario, String>("localidad"));
        // Crear la animación
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.05), tableViewUsuarios);
        transition.setByY(-20); // Mover la tabla hacia arriba
        transition.setByX(20); // Mover hacia arriba
        transition.setAutoReverse(true); // Hacer que la animación se repita en reversa
        transition.setCycleCount(2); // Repetir la animación 2 veces
        transition.play(); // Iniciar la animación
    }

    public void cambiarVentanaLocalidades(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {//Se lleva al menu que adminsitra las localidades
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaGestionLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaGestionLocalidades c = loader.getController();
        c.inicializarLocalidades();
        stage.close();
        stage.show();
    }

    public void crearUsu(MouseEvent mouseEvent) throws IOException{//LLeva a la ventana para crear un usuario
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorPersonas c= loader.getController();
        c.inicializarComboBox(false);//Se pasa una bandera para que se active o se desactive el check administrador
        stage.close();
        stage.show();
    }
}

