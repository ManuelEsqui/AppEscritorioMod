//Manuel Esquivel Sevillano 2ºDAM
package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Evento;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class controladorGestorEventos {


    public TextArea txtDescripcionAdicional;
    public Label lbTipoPrecio;
    public Label lbDescPuntoVenta;
    public TextField txtPuntoVentaEntrada;
    public TextField txtPrecio;
    public TextField txtTipo;
    @FXML
    private Button BtnVolver;

    @FXML
    private TableColumn<Evento, Integer> ColumnaId;

    @FXML
    private TableColumn<Evento, String> ColumnaNombre;

    @FXML
    private DatePicker EditFecha;

    @FXML
    private TableView<Evento> TableViewEventos;
    private ObservableList lista;


    @FXML
    private TextField txtBuscar;

    @FXML
    private TextArea txtEditDescripcion;

    @FXML
    private TextField txtEditLocalidad;

    @FXML
    private TextField txtEditNombre;

    @FXML
    private TextField txtEditUbicacion;

    @FXML
    private TextField txtId;
    int idEvento;
    private Evento eventoSelec;
    private utiles u=new utiles();
    ArrayList<Evento> arrayEventos=new ArrayList<>();
    String usu;

    public void setUsu(String usu) {
        this.usu = usu;
    }

    @FXML
    void AddEvento() throws IOException {//metodo para añadir  un evento
        u.cambiarVentanaAddEvento((Stage) txtEditLocalidad.getScene().getWindow(), usu);
    }

    @FXML
    void DeleteEvento(ActionEvent event) throws SQLException, ClassNotFoundException {//metodo para eliminar el evento

        String sid=txtId.getText();
        if (!(sid.length()<1)){
            idEvento=Integer.parseInt(sid);
            Class.forName(utiles.driver);

            // Establecemos la conexion con la BD
            Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            String sql = "DELETE FROM eventos WHERE id = ?;";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setInt(1, idEvento);
            int filas=sentencia.executeUpdate();
            System.out.println(filas+" rows afected");
            if (filas==1){
                Alert alerta=new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Eliminado");
                alerta.setHeaderText(null);
                alerta.setContentText("Evento eliminado");
                alerta.showAndWait();
            }
            conexion.close();
            sentencia.close();
            init("");
        }else {
            Alert alerta=new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText(null);
            alerta.setContentText("Debes introducir id para eliminar");
            alerta.showAndWait();
        }

    }

    @FXML
    void Editar(ActionEvent event) throws ClassNotFoundException, SQLException {//Metodo para editar el evento

        String nombre=txtEditNombre.getText();
        String idString=txtId.getText();
        String descripcion=txtEditDescripcion.getText();
        String ubicacion=txtEditUbicacion.getText();
        String localidad=txtEditLocalidad.getText();
        LocalDate datelocal = EditFecha.getValue();
        int id_loc=6;
        boolean bandera;
        if (nombre.length()<1 || idString.length()<1 || descripcion.length()<1 || ubicacion.length()<1 || localidad.length()<1 || datelocal==null){
            bandera=false;
        }else {
            bandera=true;
        }
        if (bandera){
            Date date = Date.valueOf(datelocal);
            int id=Integer.parseInt(idString);
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

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
            if (!encontrada){
                Alertas(Alert.AlertType.ERROR, "Error", "Localidad no encontrada en la base de datos, revise si existe y si no es el caso agréguela");
                return;
            }
            String sql = "UPDATE eventos SET nombre = ?, descripcion = ?, fecha = ?, localidad_id = ?, ubicacion = ? WHERE id = ?;";
            PreparedStatement sentencia=(PreparedStatement) conexion.prepareStatement(sql);
            sentencia.setString(1, nombre);
            sentencia.setString(2, descripcion);
            sentencia.setDate(3,date);
            sentencia.setInt(4, id_loc);
            sentencia.setString(5,ubicacion);
            sentencia.setInt(6,id);
            sentencia.executeUpdate();
            sentencia.close();
            sentencia2.close();
            if (txtPuntoVentaEntrada.isVisible()){
                editDatosEventoPago(id);
            } else if (txtDescripcionAdicional.isVisible()) {
                editDatosEventoGratis(id);
            }
            resul.close();
            init("");

        }else {
            Alertas(Alert.AlertType.ERROR, "Error", "Se deben rellenar todos los datos");
        }

    }

    //metodo que edita los eventos gratis
    private void editDatosEventoGratis(int idEvento) throws ClassNotFoundException, SQLException {
        if(txtTipo.getText().length()<1 || txtDescripcionAdicional.getText().length()<1){
            Alertas(Alert.AlertType.ERROR, "Ha ocurrido un error", "Deben estar rellenos todos los campos para editar el evento");
            return;
        }
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        String sql = "UPDATE eventosgratis SET tipo = ?, descripcionAdicional = ? WHERE id = ?;";
        PreparedStatement sentencia= conexion.prepareStatement(sql);
        sentencia.setString(1, txtTipo.getText());
        sentencia.setString(2, txtDescripcionAdicional.getText());
        sentencia.setInt(3, idEvento);
        sentencia.executeUpdate();
        Alertas(Alert.AlertType.INFORMATION, "Actualizacion exitosa", "El evento gratuito se ha actualizado correctamente");
    }

    //metodo que edita los eventos de pago
    private void editDatosEventoPago(int idEvento) throws ClassNotFoundException, SQLException {
        float precio=0;
        try {
            precio=Float.parseFloat(txtPrecio.getText());
            if(txtPuntoVentaEntrada.getText().length()<1){
                Alertas(Alert.AlertType.ERROR, "Ha ocurrido un error", "Debes rellenar el campo punto de venta con el lugar donde se vende la entrada");
                return;
            }
            Class.forName(utiles.driver);
            // Establecemos la conexion con la BD
            Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            String sql = "UPDATE eventosdepago SET precio = ?, venta_entrada = ? WHERE id = ?;";
            PreparedStatement sentencia= conexion.prepareStatement(sql);
            sentencia.setFloat(1, precio);
            sentencia.setString(2, txtPuntoVentaEntrada.getText());
            sentencia.setInt(3, idEvento);
            sentencia.executeUpdate();
            Alertas(Alert.AlertType.INFORMATION, "Actualizacion exitosa", "El evento de pago se ha actualizado correctamente");
        }catch (Exception e){
            Alertas(Alert.AlertType.ERROR, "Ha ocurrido un error", "El campo precio debe estar relleno por un numero");
        }


    }

    //rellena los campos de los eventos
    @FXML
    private void rellenarCampos() throws SQLException, ClassNotFoundException {
        eventoSelec=TableViewEventos.getSelectionModel().getSelectedItem();
        txtId.setText(""+eventoSelec.getId());
        for (Evento e:arrayEventos){
            if (eventoSelec.getId()==e.getId()){
                txtEditNombre.setText(e.getNombre());
                txtEditDescripcion.setText(e.getDescripcion());
                txtEditUbicacion.setText(e.getUbicacion());
                txtEditLocalidad.setText(e.getLocalidad());
                //EditFecha.setValue(e.getFecha().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                EditFecha.setValue(Instant.ofEpochMilli(e.getFecha().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
                hacerVisibleYRellenarOtrosCampos(eventoSelec.getId());
                break;
            }
        }
    }

    //posibilita la vista de los elementos unicos de cada tipo de evento
    private void hacerVisibleYRellenarOtrosCampos(int id) throws ClassNotFoundException, SQLException {
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);

        Statement sentencia2 = conexion.createStatement();
        String sql2 = "SELECT * FROM eventosgratis;";
        ResultSet resul = sentencia2.executeQuery(sql2);

        while (resul.next()) {
            if (resul.getInt(1)==id){
                lbTipoPrecio.setVisible(true);
                lbTipoPrecio.setText("Tipo:");
                lbDescPuntoVenta.setVisible(true);
                lbDescPuntoVenta.setText("Descripcion adicional:");
                txtPrecio.setVisible(false);
                txtPuntoVentaEntrada.setVisible(false);
                txtTipo.setVisible(true);
                txtDescripcionAdicional.setVisible(true);
                txtTipo.setText(resul.getString(2));
                txtDescripcionAdicional.setText(resul.getString(3));
                return;
            }
        }
        sentencia2 = conexion.createStatement();
        sql2 = "SELECT * FROM eventosdepago;";
        resul = sentencia2.executeQuery(sql2);
        while (resul.next()){
            if (resul.getInt(1)==id){
                lbTipoPrecio.setVisible(true);
                lbTipoPrecio.setText("Precio:");
                lbDescPuntoVenta.setVisible(true);
                lbDescPuntoVenta.setText("Punto de venta:");
                txtDescripcionAdicional.setVisible(false);
                txtTipo.setVisible(false);
                txtPrecio.setVisible(true);
                txtPuntoVentaEntrada.setVisible(true);
                txtPrecio.setText(resul.getString(2));
                txtPuntoVentaEntrada.setText(resul.getString(3));
                return;
            }
        }
        txtPrecio.setVisible(false);
        txtPuntoVentaEntrada.setVisible(false);
        txtDescripcionAdicional.setVisible(false);
        txtTipo.setVisible(false);
        lbTipoPrecio.setVisible(false);
        lbDescPuntoVenta.setVisible(false);
    }

    public void init(String nombreEvento) throws SQLException, ClassNotFoundException {
        inicializarTableView();//prepara la tableview
        this.lista=listAll(nombreEvento);//saca los datos
        this.TableViewEventos.setItems(lista);//inserta los datos en la tabla
    }

    //Lista los eventos, los inserta en la tabla y en un arraylist de control
    private ObservableList listAll(String nombreEvento) throws SQLException, ClassNotFoundException {
        ObservableList<Evento> listUser= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = conexion.createStatement();
        String sql2="SELECT eventos.id, eventos.nombre, descripcion, fecha, ubicacion, localidades.nombre FROM eventos INNER JOIN localidades ON eventos.localidad_id = localidades.id;";
        boolean bandera=true;//si esta bandera esta activada se resfresca el array que contiene todos los eventos
        if (nombreEvento.length()>=1){
            sql2 = "SELECT * from eventos WHERE nombre like '%"+nombreEvento+"%';";
            bandera=false;//aqui se desactiva para que no se registren eventos con este formato
        }
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {
            int id=resul.getInt(1);
            String nomb=resul.getNString(2);
            listUser.add(new Evento(id,nomb));//se inicia la lista visible
            if(bandera) arrayEventos.add(new Evento(id,nomb,resul.getString(3),resul.getString(6), resul.getString(5),resul.getDate(4)));//se inicia el array que tiene los datos
        }
        conexion.close();
        sentencia2.close();
        resul.close();

        return listUser;
    }

    private void inicializarTableView() {
        this.ColumnaId.setCellValueFactory(new PropertyValueFactory<Evento, Integer>("id"));
        this.ColumnaNombre.setCellValueFactory(new PropertyValueFactory<Evento, String>("nombre"));
    }

    //vuelve a la venta del login
    @FXML
    void VentanaLogin(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/trabajofinal_interfaces/vista/LoginView.fxml"));
        Scene escena = new Scene(root);
        Stage stage =(Stage) BtnVolver.getScene().getWindow();
        stage.setScene(escena);
        stage.close();
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    void VentanaAdminPrincipal(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        Stage stage =(Stage) BtnVolver.getScene().getWindow();
        u.cambiarVentanaAdmin(stage, usu);
    }

    @FXML
    void VentanaUsuarios(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        u.CambiarVistaEventosDesdeAdmin((Stage) BtnVolver.getScene().getWindow(), usu);
    }

    public void buscarEvento(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String nombreEvento=txtBuscar.getText();
        init(nombreEvento);
    }
}

