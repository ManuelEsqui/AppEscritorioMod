//Manuel Esquivel Sevillano 2ºDAM
package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import com.itextpdf.text.Paragraph;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

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
    utiles u=new utiles();
    private Usuario persona;
    String usu;
    public void setUsu(String usu) {
        this.usu = usu;
    }

    //inicializa los datos de la tabla usuarios
    public void init() throws SQLException, ClassNotFoundException {
        inicializarTableView();
        this.lista=listAll();
        this.tableViewUsuarios.setItems(lista);
    }

    @FXML
    void AdministrarEventos(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {//Cambia a la ventana de evenstos
        u.cambiarVentanaAdminEventos((Stage) btnVolver.getScene().getWindow(), usu);
    }

    @FXML
    void AdministrarUsuarios(ActionEvent event) throws IOException, SQLException {//Cambia a la ventana de editar usuarios
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
        c.setusuAdmin(usu);
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
    public void crearReporte(ActionEvent actionEvent) throws SQLException, ClassNotFoundException{//Genera un pdf con los datos de los usuarios
        Document document=new Document();
        try {
            String ruta=System.getProperty("user.home");//Propiedad para hacerlo en el usuario del escritorio
            PdfWriter.getInstance(document, new FileOutputStream(ruta+"/Desktop/usuariosExtreventos.pdf"));//Ruta donde se va a generar el pdf
            document.open();
            PdfPTable table=new PdfPTable(6);//Se crea la tabla
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Apellido");
            table.addCell("Edad");
            table.addCell("Estado Civil");
            table.addCell("Localidad");

            Connection cn=DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            PreparedStatement ps=cn.prepareStatement("SELECT personas.id, personas.nombre, apellidos, edad, estadoCivil, localidades.nombre FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id;");
            ResultSet rs=ps.executeQuery();//Se ejecuta la consulta
            if(rs.next()){
                do{//Se añaden los datos a cada celda
                    table.addCell(rs.getString(1));
                    table.addCell(rs.getString(2));
                    table.addCell(rs.getString(3));
                    table.addCell(String.valueOf(rs.getInt(4)));
                    table.addCell(rs.getString(5));
                    table.addCell(rs.getString(6));
                }while(rs.next());
                document.add(new Paragraph("Informe de usuarios generado: \n\n\n"));
                document.add(table);//Se añade la tabla al documento
                Date fechaActual = new Date(new java.util.Date().getTime());
                document.add(new Paragraph("\nInforme generado el "+ fechaActual));
            }
            document.close();
        }catch (Exception e){
            Alertas(Alert.AlertType.ERROR, "Error", "No se ha podido crear el informe");
        }
        init();
        Alertas(Alert.AlertType.INFORMATION, "Informe generado", "Se ha generado el informe en el escritorio y se ha actualizado la tabla");
    }
    public ObservableList<Usuario> listAll() throws ClassNotFoundException, SQLException {
        ObservableList<Usuario> listUser= FXCollections.observableArrayList();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = conexion.createStatement();
        String sql2 = "SELECT personas.nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, localidades.nombre FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id WHERE personas.admin=false;";//Selecciona los datos de todas las personas
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
    //inicializa las columnas de la tabla además de hacer una animación
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
        u.cambiarVentanaLocalidades((Stage) btnVolver.getScene().getWindow(), usu);
    }

    public void crearUsu(MouseEvent mouseEvent) throws IOException, SQLException {//LLeva a la ventana para crear un usuario
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/GestorPersonasView.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) btnVolver.getScene().getWindow();
        stage.setScene(escena);
        controladorGestorPersonas c= loader.getController();
        c.setUsu(usu);
        c.inicializarComboBox(false);//Se pasa una bandera para que se active o se desactive el check administrador
        stage.close();
        stage.show();
    }
    public void ventanaEditarUsuarios(MouseEvent mouseEvent) throws IOException, SQLException, ClassNotFoundException {
        boolean bandera=true;
        String sql = "SELECT personas.nombre, apellidos, sexo, estadoCivil, user, passwrd, edad, localidades.nombre FROM personas INNER JOIN localidades ON personas.localidad_id = localidades.id;";
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = conexion.createStatement();
        ResultSet result = sentencia2.executeQuery(sql);
        try {
            while (result.next()) {
                if (usu.equals(result.getString(5))){
                    persona=new Usuario(result.getString(1), result.getString(2), result.getString(3), result.getString(4), result.getString(5),result.getString(8), result.getString(6), result.getInt(7));
                }
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaEdicionUsuarios.fxml"));
            Parent root=loader.load();
            Scene escena = new Scene(root);
            Stage stage =(Stage) btnVolver.getScene().getWindow();
            stage.setScene(escena);
            ControladorVentanaEdicionUsuarios c = (ControladorVentanaEdicionUsuarios) loader.getController();
            c.setBandera(bandera);
            c.setusuAdmin(usu);
            c.setUsuario(persona);
            stage.close();
            stage.show();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Volver");
            alert.setHeaderText("Esta funcion ha sido bloqueda");
            alert.setContentText("Para editar tu cuenta debe volver a iniciar sesión.\n¿Desea volver?");
            Optional<ButtonType> resultado = alert.showAndWait();
            if (resultado.get() == ButtonType.OK){
                new utiles().cambiarVentanaLogin((Stage) btnVolver.getScene().getWindow());
            }

        }

    }
}

