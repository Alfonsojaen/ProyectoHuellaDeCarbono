package github.alfonsojaen.view;

import github.alfonsojaen.dao.RecomendacionDAO;
import github.alfonsojaen.entities.*;
import github.alfonsojaen.services.HabitoService;
import github.alfonsojaen.services.RecomendacionService;
import github.alfonsojaen.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
    private RecomendacionService recomendacionService;

    public ControllerShowHabitos() {
        habitoService = new HabitoService();
        recomendacionService = new RecomendacionService();
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

    @FXML
    public void mostrarRecomendaciones() {
        Habito habitoSeleccionado = tableHabitos.getSelectionModel().getSelectedItem();

        if (habitoSeleccionado != null) {
            Actividad actividad = habitoSeleccionado.getIdActividad();
            Categoria categoria = actividad.getIdCategoria();
            int idCategoria = categoria.getId();

            List<Recomendacion> recomendaciones = recomendacionService.findRecomendacionesByCategoria(idCategoria);

            if (!recomendaciones.isEmpty()) {
                StringBuilder recomendacionesTexto = new StringBuilder();
                recomendaciones.forEach(recomendacion -> {
                    recomendacionesTexto.append(recomendacion.getDescripcion()).append("\n");
                });

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Recomendaciones");
                alert.setHeaderText("Recomendaciones para la actividad: " + actividad.getNombre());
                alert.setContentText(recomendacionesTexto.toString());

                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Sin Recomendaciones");
                alert.setHeaderText("No hay recomendaciones para esta actividad.");
                alert.setContentText("Lo sentimos, no se han encontrado recomendaciones para la actividad seleccionada.");

                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ningún hábito seleccionado");
            alert.setContentText("Por favor, selecciona un hábito de la tabla.");

            alert.showAndWait();
        }
    }
}