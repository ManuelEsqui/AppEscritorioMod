package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento;
import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
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
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.*;
import java.util.Date;

public class controladorVentanaUsuarios {

    @FXML
    private Button btnInfo;

    @FXML
    private Button btnVolver;

    @FXML
    private TableColumn<Evento, String> columnaDescripcion;

    @FXML
    private TableColumn<Evento, Date> columnaFecha;

    @FXML
    private TableColumn<Evento, String> columnaLocalidad;

    @FXML
    private TableColumn<Evento, String> columnaNombre;

    @FXML
    private TableColumn<Evento, String> columnaUbicacion;

    @FXML
    private TableView<Evento> tablaEventos;
    private ObservableList lista;
    String usu;
    String contra;
    int id_usuario=0;
    private Evento eventoSelec;

    public void setContra(String contra) throws SQLException, ClassNotFoundException {
        this.contra = contra;
        init();
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    @FXML
    void apuntarseAEvento(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            int id_evento=0;
            eventoSelec=tablaEventos.getSelectionModel().getSelectedItem();
            if(eventoSelec!=null){
                Result result = getResult();
                while (result.resul().next()) {
                    id_evento= result.resul().getInt(1);
                }
                result.sentencia2().close();
                result.resul().close();
                if (usu!=null && contra!=null){//consulta para extraer la id
                    Statement sentencia3 = (Statement) result.conexion().createStatement();
                    String sql3 = "SELECT id FROM personas WHERE user LIKE '"+usu+"' AND passwrd LIKE '"+contra+"';";
                    ResultSet resul2 = sentencia3.executeQuery(sql3);
                    while (resul2.next()) {
                        id_usuario=resul2.getInt(1);
                    }
                    sentencia3.close();
                    resul2.close();
                    if (id_usuario!=0 && id_evento!=0){//consulta para insertar en tabla intermedia con las id
                        String sql = "INSERT INTO Persona_Evento (id_Persona, id_Evento) VALUES (?, ?);";
                        PreparedStatement sentencia=(PreparedStatement) result.conexion().prepareStatement(sql);
                        sentencia.setInt(1, id_usuario);
                        sentencia.setInt(2, id_evento);
                        sentencia.executeUpdate();

                        msgError(Alert.AlertType.WARNING, "Apuntado", "Has sido añadido a la lista de participantes del evento");

                        result.conexion().close();
                        sentencia.close();
                    }else{
                        msgError(Alert.AlertType.ERROR, "Error", "Ha ocurrido un error");
                    }
                }else{
                    msgError(Alert.AlertType.ERROR, "Error", "Un administrador no puede apuntarse a eventos");
                }

            }else{
                msgError(Alert.AlertType.ERROR, "Nigun evento seleccionado", "Ningun evento seleccionado");
            }
        }catch (Exception e){
            msgError(Alert.AlertType.ERROR, "Error", "Ya estas apuntado a este evento");
        }
    }

    private static void msgError(Alert.AlertType error, String Nigun_evento_seleccionado, String Ningun_evento_seleccionado) {
        Alert alerta = new Alert(error);
        alerta.setTitle(Nigun_evento_seleccionado);
        alerta.setHeaderText(null);
        alerta.setContentText(Ningun_evento_seleccionado);
        alerta.showAndWait();
    }

    private @NotNull Result getResult() throws ClassNotFoundException, SQLException {
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        //consulta para extraer la id
        String sql2 = "SELECT id FROM eventos WHERE nombre LIKE '"+eventoSelec.getNombre()+"' AND descripcion LIKE '"+eventoSelec.getDescripcion()+"';";
        ResultSet resul = sentencia2.executeQuery(sql2);
        Result result = new Result(conexion, sentencia2, resul);
        return result;
    }

    private record Result(Connection conexion, Statement sentencia2, ResultSet resul) {
    }

    @FXML
    void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        init();
    }

    private void init() throws ClassNotFoundException, SQLException {
        inicializarTableView();
        this.lista=listAll();
        this.tablaEventos.setItems(lista);
    }

    private ObservableList listAll() throws ClassNotFoundException, SQLException {//metodo para añadir valores a la tabla
        ObservableList<Evento> observableList= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT eventos.nombre, descripcion, fecha, ubicacion, localidades.nombre FROM eventos INNER JOIN localidades ON eventos.localidad_id = localidades.id;";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            String nomb=resul.getNString(1);
            String descripcion=resul.getNString(2);
            Date fecha=resul.getDate(3);
            String ubicacion=resul.getNString(4);
            String loc_nombre=resul.getNString(5);
            observableList.add(new Evento(nomb,descripcion,loc_nombre,ubicacion,fecha));
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return observableList;
    }

    private void inicializarTableView() {//metodo para inicializar cada una de las columnas
        this.columnaNombre.setCellValueFactory(new PropertyValueFactory<Evento, String>("nombre"));
        this.columnaDescripcion.setCellValueFactory(new PropertyValueFactory<Evento, String>("descripcion"));
        this.columnaUbicacion.setCellValueFactory(new PropertyValueFactory<Evento, String>("ubicacion"));
        this.columnaLocalidad.setCellValueFactory(new PropertyValueFactory<Evento,String>("localidad"));
        this.columnaFecha.setCellValueFactory(new PropertyValueFactory<Evento, Date>("fecha"));
    }

    @FXML
    void volverLogin(ActionEvent event) throws IOException {
        if (usu!=null && contra!=null){
            Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
            Scene escena = new Scene(root);
            Stage stage =(Stage) btnVolver.getScene().getWindow();
            stage.setScene(escena);
            stage.close();
            stage.show();
        }else{
            new utiles().cambiarVentanaAdminEventos((Stage) btnVolver.getScene().getWindow());
        }


    }

    public void ventanaMisEventos(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/EventosUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnInfo.getScene().getWindow();
        stage.setScene(escena);
        controladorEventosUsuarios c = loader.getController();
        c.setUsu(usu);
        c.setContra(contra);
        stage.close();
        stage.show();
    }

    public void informaciónDeEvento(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        int id_evento = 0;
        eventoSelec=tablaEventos.getSelectionModel().getSelectedItem();
        if(eventoSelec!=null) {
            Result result = getResult();
            while (result.resul().next()) {
                id_evento = result.resul().getInt(1);
            }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaInfoEventos.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnInfo.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaInfoEventos c = (controladorVentanaInfoEventos)loader.getController();
        c.setNombreEvento(id_evento);
        c.setUsu(usu);
        c.setContra(contra);
        stage.close();
        stage.show();
        }else{
            msgError(Alert.AlertType.ERROR, "Nigun evento seleccionado", "Ningun evento seleccionado");
        }
    }
}

