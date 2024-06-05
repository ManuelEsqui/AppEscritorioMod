package com.example.trabajofinal_interfaces.controlador;

import com.example.trabajofinal_interfaces.utiles.utiles;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;

import static com.example.trabajofinal_interfaces.utiles.utiles.Alertas;

public class ControladorVentanaAgregarEditarLocalidades {

    @FXML
    private Button aceptar;

    @FXML
    private ImageView imagenPreview;

    @FXML
    private Label lbAccion;

    @FXML
    private Label preview;

    @FXML
    private TextField rutaImagen;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtprovincia;

    @FXML
    private Button volver;
    private boolean bandera;
    File imagenSeleccionada;
    private int id_loc;
    private  String usu;

    public void setUsu(String usu) {
        this.usu = usu;
    }

    void inicializar(boolean bandera){
        this.bandera=bandera;//Detectar que quiere hacer
    }

    @FXML
    void aceptar(ActionEvent event) {
        if (bandera){//Añadir
            try (Connection conexion = DriverManager.getConnection(utiles.url,utiles.usuario, utiles.clave)) {
                imagenSeleccionada = new File(rutaImagen.getText()); // Ruta de la imagen a subir
                FileInputStream fis = new FileInputStream(imagenSeleccionada);
                if (txtNombre.getText().isEmpty() && txtprovincia.getText().isEmpty()){
                    Alertas(Alert.AlertType.ERROR, "Error", "No pueden haber campos vacios");
                    return;
                }
                PreparedStatement statement = conexion.prepareStatement("INSERT INTO localidades (nombre, provincia, imagen) VALUES (?, ?, ?)");
                statement.setString(1, txtNombre.getText());
                statement.setString(2, txtprovincia.getText());
                statement.setBinaryStream(3, fis, (int) imagenSeleccionada.length());
                statement.executeUpdate();

                Alertas(Alert.AlertType.INFORMATION, "Añadida con exito", "La localidad ha sido añadida exitosamente");
                volverVentanaAnterior();

            } catch (SQLException e) {
                Alertas(Alert.AlertType.ERROR, "Error", "No se pudo conectar a la base de datos");
            } catch (FileNotFoundException e) {
                Alertas(Alert.AlertType.ERROR, "Error", "No se pudo encontrar la imagen");
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

        } else{// Este es el modo para editar las personas

            try (Connection conexion = DriverManager.getConnection(utiles.url,utiles.usuario, utiles.clave)) {
                imagenSeleccionada = new File(rutaImagen.getText()); // Ruta de la imagen
                FileInputStream fis = new FileInputStream(imagenSeleccionada);

                Statement sentencia2 = (Statement) conexion.createStatement();
                String sql2 = "SELECT * FROM Localidades;";
                ResultSet resul = sentencia2.executeQuery(sql2);

                // Recorremos el resultado para visualizar cada fila
                // Se hace un bucle mientras haya registros
                while (resul.next()) {

                    if (resul.getString(2).equalsIgnoreCase(txtNombre.getText())){//Se obtiene el id comparando el nombre de la localidad con la base de datos
                        id_loc=resul.getInt(1);
                    }

                }

                PreparedStatement statement = conexion.prepareStatement("UPDATE localidades SET nombre = ?, provincia = ?, imagen = ? WHERE id = ?;");
                statement.setString(1, txtNombre.getText());
                statement.setString(2, txtprovincia.getText());
                statement.setBinaryStream(3, fis, (int) imagenSeleccionada.length());
                statement.setInt(4, id_loc);
                statement.executeUpdate();

                Alertas(Alert.AlertType.INFORMATION, "Editada con exito", "La localidad ha sido editada exitosamente");
                volverVentanaAnterior();

            } catch (SQLException e) {
                Alertas(Alert.AlertType.ERROR, "Error", "No se pudo conectar a la base de datos");
            } catch (FileNotFoundException e) {
                Alertas(Alert.AlertType.ERROR, "Error", "No se pudo encontrar la imagen");
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    void volver(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {//Vuelve a la venatana anterior
        volverVentanaAnterior();
    }

    private void volverVentanaAnterior() throws IOException, ClassNotFoundException, SQLException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/trabajofinal_interfaces/vista/VentanaGestionLocalidades.fxml"));
        Parent root=loader.load();
        Scene escena = new Scene(root);
        Stage stage =(Stage) volver.getScene().getWindow();
        stage.setScene(escena);
        controladorVentanaGestionLocalidades c = (controladorVentanaGestionLocalidades) loader.getController();
        c.setUsu(usu);
        c.inicializarLocalidades();//metdo para refescar la lista
        stage.close();
        stage.show();
    }

    public void rellenarCampos(String nombre, String provincia) throws SQLException {
        lbAccion.setText("¡Edita la localidad!");
        txtNombre.setText(nombre);
        txtprovincia.setText(provincia);
        Connection conexion = DriverManager.getConnection(utiles.url,utiles.usuario, utiles.clave);
        Statement sentencia2 =conexion.createStatement();
        String sql2 = "SELECT * FROM Localidades;";
        ResultSet resul = sentencia2.executeQuery(sql2);

        // Recorremos el resultado para visualizar cada fila
        // Se hace un bucle mientras haya registros
        while (resul.next()) {

            if (resul.getString(2).equalsIgnoreCase(txtNombre.getText())){
                id_loc=resul.getInt(1);
                byte[] imagenBytes = resul.getBytes(4);
                Image imagen = new Image(new java.io.ByteArrayInputStream(imagenBytes));
                imagenPreview.setImage(imagen);
            }

        }

    }

    public void selecImagen(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        imagenSeleccionada = fileChooser.showOpenDialog(rutaImagen.getScene().getWindow());
        rutaImagen.setText(imagenSeleccionada.toString());
        if (imagenSeleccionada != null) {
            String rutaImagen = imagenSeleccionada.toURI().toString();
            Image imagen = new Image(rutaImagen);
            imagenPreview.setImage(imagen);
        }
    }
}

