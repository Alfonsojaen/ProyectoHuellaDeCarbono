package github.alfonsojaen.view;

import github.alfonsojaen.entities.Habito;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.HabitoService;
import github.alfonsojaen.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ControllerShowHabitos {

    @FXML
    private TableView<Habito> tableHabitos;

    @FXML
    private TableColumn<Habito, Integer> colIdActividad;

    @FXML
    private TableColumn<Habito, Integer> colFrecuencia;

    @FXML
    private TableColumn<Habito, String> colTipo;

    @FXML
    private TableColumn<Habito, String> colUltimaFecha;

    private HabitoService habitoService;

    public ControllerShowHabitos() {
        habitoService = new HabitoService();
    }

    @FXML
    public void initialize() {
        colIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        colFrecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUltimaFecha.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));

        cargarHabitos();
    }

    private void cargarHabitos() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Habito> listaHabitos = habitoService.getAllHabitos();
            List<Habito> listaFiltrada = listaHabitos.stream().filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId())).collect(Collectors.toList());

            ObservableList<Habito> habitos = FXCollections.observableArrayList(listaFiltrada);
            tableHabitos.setItems(habitos);
        }
    }
}
