//Manuel Esquivel Sevillano 2ºDAM
package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Usuario;
import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class controladorVentanaInfoEventos {
    public Label total;
    @FXML
    private ListView<String> listViewNombre;
    @FXML
    private ListView<String> listViewApellido;

    @FXML
    private ListView<Integer> listViewEdad;

    @FXML
    private ListView<String> listViewEstado;
    @FXML
    private ListView<String> listViewSexo;
    private ArrayList<Usuario> usuarios;
    private ArrayList<String> arrayNombre=new ArrayList<>();
    private ArrayList<String> arrayApellido=new ArrayList<>();
    private ArrayList<Integer> arrayEdad=new ArrayList<>();
    private ArrayList<String> arraySexo=new ArrayList<>();
    private ArrayList<String> arrayEstado=new ArrayList<>();
    private int idEvento;
    private String contra, usu;

    public void setNombreEvento(int id) throws SQLException, ClassNotFoundException {
        this.idEvento = id;
        initListView();
    }
    public void setContra(String contra) {
        this.contra = contra;
    }

    public void setUsu(String usu) {
        this.usu = usu;
    }

    private void initListView() throws SQLException, ClassNotFoundException {
        initArrayPersonas();
        initarrayString();
        listViewNombre.setItems(FXCollections.observableArrayList(arrayNombre));
        listViewApellido.setItems(FXCollections.observableArrayList(arrayApellido));
        listViewEdad.setItems(FXCollections.observableArrayList(arrayEdad));
        listViewSexo.setItems(FXCollections.observableArrayList(arraySexo));
        listViewEstado.setItems(FXCollections.observableArrayList(arrayEstado));
    }

    private void initarrayString() {
        int contador=0;
        for (Usuario usuario : usuarios) {
            arrayNombre.add(usuario.getNombre());
            arrayApellido.add(usuario.getApellidos());
            arrayEdad.add(usuario.getEdad());
            arraySexo.add(usuario.getSexo());
            arrayEstado.add(usuario.getEstadoCivil());
            contador++;
        }
        total.setText("Total de asistentes : " + contador);
    }

    private void initArrayPersonas() throws ClassNotFoundException, SQLException {
        usuarios = new ArrayList<>();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();

        String sql2 = "SELECT p.nombre, apellidos, sexo, estadoCivil, edad FROM personas p INNER JOIN persona_evento pe ON p.id = pe.id_persona INNER JOIN eventos e ON pe.id_evento = e.id where e.id = '"+idEvento+"';";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {// Extraer los datos de la base de datos
            String nomb=resul.getNString(1);
            String apell=resul.getNString(2);
            String sex=resul.getNString(3);
            String estado=resul.getNString(4);
            int ed=resul.getInt(5);
            usuarios.add(new Usuario(nomb,apell,sex,estado,ed));
        }
        conexion.close();
        sentencia2.close();
        resul.close();
    }

    public void volverPagAnterio(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) listViewApellido.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaUsuarios c = loader.getController();
        c.setUsu(usu);
        c.setContra(contra);
        stage.close();
        stage.show();
    }

    public void volverLogin(ActionEvent actionEvent) throws IOException {
        new utiles().cambiarVentanaLogin((Stage) listViewApellido.getScene().getWindow());
    }

    public void volverMisEventos(ActionEvent actionEvent) throws SQLException, ClassNotFoundException, IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/EventosUsuarios.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) listViewSexo.getScene().getWindow();
        stage.setScene(escena);
        controladorEventosUsuarios c = loader.getController();
        c.setUsu(usu);
        c.setContra(contra);
        stage.close();
        stage.show();
    }
}
