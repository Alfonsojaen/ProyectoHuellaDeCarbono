package github.alfonsojaen.view;

import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerDeleteHuella {

    @FXML
    private ComboBox<Huella> cHuella;

    @FXML
    private Label labelInfo;

    @FXML
    private Button btnEliminar;

    private HuellaService huellaService;

    public ControllerDeleteHuella() {
        huellaService = new HuellaService();
    }

    @FXML
    public void initialize() {
        cargarHuellasUsuario();

        cHuella.setOnAction(event -> {
            Huella huellaSeleccionada = cHuella.getValue();
            if (huellaSeleccionada != null) {
                mostrarInfoHuella(huellaSeleccionada);
            }
        });
    }

    private void cargarHuellasUsuario() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioActual != null) {
            List<Huella> listaHuellas = huellaService.getAllHuellas();
            List<Huella> listaFiltrada = listaHuellas.stream().filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId())).collect(Collectors.toList());

            cHuella.getItems().setAll(listaFiltrada);

        } else {
            Utils.ShowAlert("Error, no hay un usuario logueado.");
        }
    }

    private void mostrarInfoHuella(Huella huella) {
        labelInfo.setText("ID: " + huella.getId() + " | Valor: " + huella.getValor() + " | Unidad: " + huella.getUnidad());
    }

    @FXML
    private void btEliminarHuella() {
        Huella huellaSeleccionada = cHuella.getValue();
        if (huellaSeleccionada == null) {
            Utils.ShowAlert("Error, Seleccione una huella para eliminar.");
            return;
        }

        boolean eliminada = huellaService.eliminarHuella(huellaSeleccionada);

        if (eliminada) {
            Utils.ShowAlert("Éxito, la huella ha sido eliminada correctamente.");
            cHuella.getItems().remove(huellaSeleccionada); // Actualiza el ComboBox
            labelInfo.setText("");
        } else {
            Utils.ShowAlert("Error, no se pudo eliminar la huella. Intente nuevamente.");
        }
    }

    @FXML
    private void switchToShowHuellas() throws IOException {
        Scenes.setRoot("pantallaHuellas");
    }
}