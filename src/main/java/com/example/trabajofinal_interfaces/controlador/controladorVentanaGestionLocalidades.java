package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.modelo.Localidad;
import com.example.trabajofinal_interfaces.utiles.utiles;
import com.itextpdf.text.Paragraph;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class controladorVentanaGestionLocalidades {

    @FXML
    private ImageView imagen;

    @FXML
    private ListView<String> listViewLocalidades;
    ArrayList<Localidad> localidades;
    ArrayList<String> localidadesNombre;

    @FXML
    private MenuButton menuButton;

    @FXML
    private Label provincia;
    utiles utiles = new utiles();
    String usu;

    public void setUsu(String usu) {
        this.usu = usu;
    }

    @FXML
    void add(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAgregarEditarLocalidades.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaAgregarEditarLocalidades c = (ControladorVentanaAgregarEditarLocalidades) loader.getController();
        c.setUsu(usu);
        c.inicializar(true);
        stage.close();
        stage.show();
    }

    @FXML
    void delete(ActionEvent event) throws SQLException, ClassNotFoundException {
        Localidad localidad = null;
        try {
            localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            Alertas(Alert.AlertType.ERROR, "Selecciona una localidad", "Debes selaccionar una localidad para eliminarla");
            return;
        }
        if (localidad.getNombre().equalsIgnoreCase("no registrada")) {
            Alertas(Alert.AlertType.ERROR, "NO", "Es imposible eliminar este registro");
            return;
        }
        // Cargar el driver
        Class.forName(utiles.driver);

        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        //Se hace la consulta para eliminar segun el usuario y la contraseña
        String sql = "DELETE FROM localidades WHERE nombre like ? AND provincia like ?;";
        PreparedStatement sentencia = (PreparedStatement) conexion.prepareStatement(sql);
        //Se introducen los parametros en el preparedStatement
        sentencia.setString(1, localidad.getNombre());
        sentencia.setString(2, localidad.getProvincia());
        int filas = sentencia.executeUpdate();
        System.out.println(filas + " rows afected");
        if (filas < 1) {
            Alertas(Alert.AlertType.ERROR, "Fallo", "Ha ocurrido un error");
        } else {
            Alertas(Alert.AlertType.INFORMATION, "Localidad eliminada", "Has eliminado la localidad de: " + localidad.getNombre());
        }
        inicializarLocalidades();
    }

    //metodo que selecciona una localidad de la lista y la prepera para editarla en la siguiente ventana
    @FXML
    void update(ActionEvent event) throws IOException, SQLException {
        Localidad localidad;
        try {
            localidad = localidades.get(listViewLocalidades.getSelectionModel().getSelectedIndex());
        } catch (Exception e) {
            Alertas(Alert.AlertType.ERROR, "Error", "Debes seleccionar una localidad para editarla");
            return;
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaAgregarEditarLocalidades.fxml"));
        Parent root = loader.load();
        Scene escena = new Scene(root);
        Stage stage = (Stage) menuButton.getScene().getWindow();
        stage.setScene(escena);
        ControladorVentanaAgregarEditarLocalidades c = loader.getController();
        c.inicializar(false);
        c.setUsu(usu);
        c.rellenarCampos(localidad.getNombre(), localidad.getProvincia());
        stage.close();
        stage.show();
    }

    //metodo para llenar la lista de localidades
    public void inicializarLocalidades() throws ClassNotFoundException, SQLException {
        localidades = new ArrayList<>();
        localidadesNombre = new ArrayList<>();
        // Cargar el driver
        Class.forName(utiles.driver);
        // Establecemos la conexion con la BD
        Connection conexion = (Connection) DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
        Statement sentencia2 = (Statement) conexion.createStatement();
        String sql2 = "SELECT * FROM Localidades;";
        ResultSet resul = sentencia2.executeQuery(sql2);
        while (resul.next()) {

            byte[] imagenBytes = resul.getBytes(4);
            Image imagen = new Image(new java.io.ByteArrayInputStream(imagenBytes));
            localidades.add(new Localidad(resul.getString(2), resul.getString(3), imagen));
            //localidadesNombre.add(localidades.get(cont).getNombre());
            localidadesNombre.add(resul.getString(2));
        }
        listViewLocalidades.setItems(FXCollections.observableArrayList(localidadesNombre));
    }

    public void selecionarCiudad(MouseEvent mouseEvent) {
        String nombre = listViewLocalidades.getSelectionModel().getSelectedItem();
        for (Localidad localidad : localidades) {
            if (localidad.getNombre().equals(nombre)) {
                provincia.setText("Esta en la provincia de: " + localidad.getProvincia());
                imagen.setImage(localidad.getImage());
            }
        }
    }

    public void volver(ActionEvent actionEvent) throws SQLException, IOException, ClassNotFoundException {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        utiles.cambiarVentanaAdmin(stage, usu);
    }

    public void ventanaEventos(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        utiles.cambiarVentanaAdminEventos(stage, usu);
    }

    public void volverLogIn(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) menuButton.getScene().getWindow();
        utiles.cambiarVentanaLogin(stage);
    }

    //metodo para generar un pdf en el escritorio con la información de las localidades
    public void generarReporte(ActionEvent actionEvent) {
        Document document = new Document();
        try {
            String ruta = System.getProperty("user.home");//Propiedad para hacerlo en el usuario del escritorio
            PdfWriter.getInstance(document, new FileOutputStream(ruta + "/Desktop/LocalidadesExtreventos.pdf"));//Ruta donde se va a generar el pdf
            document.open();
            PdfPTable table = new PdfPTable(3);//Se crea la tabla
            table.addCell("ID");
            table.addCell("Nombre");
            table.addCell("Provincia");
            Connection cn = DriverManager.getConnection(utiles.url, utiles.usuario, utiles.clave);
            PreparedStatement ps = cn.prepareStatement("SELECT id, nombre, provincia FROM localidades;");
            ResultSet rs = ps.executeQuery();//Se ejecuta la consulta
            if (rs.next()) {
                do {//Se añaden los datos a cada celda
                    table.addCell(rs.getString(1));
                    table.addCell(rs.getString(2));
                    table.addCell(rs.getString(3));
                } while (rs.next());
                document.add(new Paragraph("Informe de localidades generado: \n\n\n"));
                document.add(table);
                document.add(new Paragraph("\nInforme generado el " + new Date(new java.util.Date().getTime())));
            }
            document.close();
            Alertas(Alert.AlertType.INFORMATION, "Informe generado", "Se ha generado el informe en el escritorio");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
