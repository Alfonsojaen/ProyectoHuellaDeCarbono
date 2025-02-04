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
import java.time.LocalDate;
import java.util.List;

public class ControllerInsertHuella {

    @FXML
    private TextField tValor;
    @FXML
    private ComboBox<Actividad> cActividad;
    @FXML
    private Label labelUnidad;
    @FXML
    private DatePicker dFecha;

    private ActividadService actividadService;
    private HuellaService huellaService;

    public ControllerInsertHuella() {
        huellaService = new HuellaService();
        actividadService = new ActividadService();
    }

    @FXML
    public void initialize() {
        List<Actividad> actividadesDisponibles = actividadService.getAllActividades();
        cActividad.getItems().addAll(actividadesDisponibles);

        cActividad.setOnAction(event -> {
            Actividad actividadSeleccionada = cActividad.getValue();
            if (actividadSeleccionada != null) {
                mostrarUnidadDeActividad(actividadSeleccionada);
            }
        });
        configurarCampoNumerico(tValor);
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
        LocalDate fechaSeleccionada = dFecha.getValue();

        if (valor.isEmpty() || actividadSeleccionada == null || fechaSeleccionada == null) {
            Utils.ShowAlert("Error, Campos vacíos, Por favor, complete todos los campos.");
            return;
        }

        if (fechaSeleccionada.isAfter(LocalDate.now())) {
            Utils.ShowAlert("Error, Fecha inválida, La fecha no puede ser posterior al día de hoy.");
            return;
        }

        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();

        Huella huella = new Huella();
        huella.setValor(new BigDecimal(valor));
        huella.setUnidad(labelUnidad.getText());
        huella.setIdActividad(actividadSeleccionada);
        huella.setIdUsuario(usuarioLogueado);
        huella.setFecha(fechaSeleccionada);

        boolean creada = huellaService.createHuella(huella);

        if (creada) {
            Utils.ShowAlert("Registro exitoso, La huella ha sido creada correctamente.");
        } else {
            Utils.ShowAlert("Error de registro, La huella no pudo ser creada. Verifique los datos y vuelva a intentarlo.");
        }
    }

    private void configurarCampoNumerico(TextField textField) {
        textField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("([0-9]*)[\\.]?[0-9]*")) {
                return change;
            }
            return null;
        }));
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