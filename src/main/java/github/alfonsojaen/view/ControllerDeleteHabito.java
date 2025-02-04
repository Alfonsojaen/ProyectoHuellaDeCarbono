package github.alfonsojaen.view;

import github.alfonsojaen.entities.Habito;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.HabitoService;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerDeleteHabito {

    @FXML
    private ComboBox<Habito> cHabito;

    @FXML
    private Label labelInfo;

    @FXML
    private Button btnEliminar;

    private HabitoService habitoService;

    public ControllerDeleteHabito() {
        habitoService = new HabitoService();
    }

    @FXML
    public void initialize() {
        cargarHabitosUsuario();

        cHabito.setOnAction(event -> {
            Habito habitoSeleccionado = cHabito.getValue();
            if (habitoSeleccionado != null) {
                mostrarInfoHabito(habitoSeleccionado);
            }
        });
    }

    private void cargarHabitosUsuario() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioActual != null) {
            List<Habito> habitosUsuario = habitoService.getAllHabitos().stream()
                    .filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());
            cHabito.getItems().addAll(habitosUsuario);
        }
    }

    private void mostrarInfoHabito(Habito habito) {
        labelInfo.setText("Frecuencia: " + habito.getFrecuencia() + " | Tipo: " + habito.getTipo() + " | Última Fecha: " + habito.getUltimaFecha());
    }

    @FXML
    private void btEliminarHabito() {
        Habito habitoSeleccionado = cHabito.getValue();
        if (habitoSeleccionado == null) {
            Utils.ShowAlert("Error, Seleccione un hábito para eliminarlo.");
            return;
        }

        boolean eliminado = habitoService.eliminarHabito(habitoSeleccionado);

        if (eliminado) {
            Utils.ShowAlert("Éxito, el hábito ha sido eliminado correctamente.");
            cHabito.getItems().remove(habitoSeleccionado); // Eliminar de la lista del ComboBox
            labelInfo.setText(""); // Limpiar la información mostrada
        } else {
            Utils.ShowAlert("Error, No se pudo eliminar el hábito. Intente nuevamente.");
        }
    }

    @FXML
    private void switchToShowHabito() throws IOException {
        Scenes.setRoot("pantallaHabitos");
    }
}