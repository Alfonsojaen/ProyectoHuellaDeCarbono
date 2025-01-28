package github.alfonsojaen.view;

import github.alfonsojaen.entities.Actividad;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.ActividadService;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class ControllerInsertHuella {

    @FXML
    private TextField tValor;
    @FXML
    private ComboBox<Actividad> cActividad;
    @FXML
    private Label labelUnidad;

    private ActividadService actividadService;
    private HuellaService huellaService;

    public ControllerInsertHuella() {
        huellaService = new HuellaService();
        actividadService = new ActividadService();
    }

    @FXML
    public void initialize() {
        List<Actividad> actividadesDisponibles = actividadService.obtenerActividades();
        cActividad.getItems().addAll(actividadesDisponibles);

        cActividad.setOnAction(event -> {
            Actividad actividadSeleccionada = cActividad.getValue();
            if (actividadSeleccionada != null) {
                mostrarUnidadDeActividad(actividadSeleccionada);
            }
        });
    }

    private void mostrarUnidadDeActividad(Actividad actividad) {
        List<String> unidades = actividadService.findUnidadByActividad(actividad.getIdCategoria().getId());
        if (!unidades.isEmpty()) {
            labelUnidad.setText(unidades.get(0));
        } else {
            labelUnidad.setText("Unidad no encontrada");
        }
    }

    @FXML
    public void btCrearHuella() {
        addHuella();
    }

    @FXML
    private void addHuella() {
        String valor = tValor.getText();
        Actividad actividadSeleccionada = cActividad.getValue();

        if (valor.isEmpty() || actividadSeleccionada == null) {
            showAlert("Error", "Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioLogueado == null) {
            showAlert("Error", "Usuario no iniciado", "Debe iniciar sesión para crear una huella.", Alert.AlertType.ERROR);
            return;
        }

        Huella huella = new Huella();
        huella.setValor(new BigDecimal(valor));
        huella.setUnidad(labelUnidad.getText());
        huella.setIdActividad(actividadSeleccionada);
        huella.setIdUsuario(usuarioLogueado);
        huella.setFecha(Instant.now());

        boolean creada = huellaService.crearHuella(huella);

        if (creada) {
            Utils.ShowAlert("Registro exitoso, La huella ha sido creada correctamente.");
        } else {
            Utils.ShowAlert("Error de registro, La huella no pudo ser creada. Verifique los datos y vuelva a intentarlo.");
        }
    }

    private void showAlert(String title, String header, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void switchToAnotherScene() {
        try {
            Scenes.setRoot("otraEscena");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}