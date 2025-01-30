package github.alfonsojaen.view;

import github.alfonsojaen.entities.*;
import github.alfonsojaen.services.ActividadService;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.services.RecomendacionService;
import github.alfonsojaen.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerShowHuellas {

    @FXML
    private TableView<Huella> tableHuellas;

    @FXML
    private TableColumn<Huella, Integer> colIdActividad;

    @FXML
    private TableColumn<Huella, Double> colValor;

    @FXML
    private TableColumn<Huella, String> colUnidad;

    @FXML
    private TableColumn<Huella, String> colFecha;

    @FXML
    private Button btnMostrarHuella;

    @FXML
    private Button btnMostrarRecomendaciones;

    @FXML
    private Button btnCrearHuella;

    private HuellaService huellaService;
    private ActividadService actividadService;
    private RecomendacionService recomendacionService;

    public ControllerShowHuellas() {
        huellaService = new HuellaService();
        actividadService = new ActividadService();
        recomendacionService = new RecomendacionService();
    }

    @FXML
    public void initialize() {
        colIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        cargarHuellas();
    }

    private void cargarHuellas() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Huella> listaHuellas = huellaService.getAllHuellas();
            List<Huella> listaFiltrada = listaHuellas.stream().filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId())).collect(Collectors.toList());

            ObservableList<Huella> huellas = FXCollections.observableArrayList(listaFiltrada);
            tableHuellas.setItems(huellas);
        }
    }

    @FXML
    public void mostrarHuellaCarbono() {
        Huella huellaSeleccionada = tableHuellas.getSelectionModel().getSelectedItem();

        if (huellaSeleccionada != null) {
            int idActividad = huellaSeleccionada.getIdActividad().getId();

            BigDecimal factorEmision = actividadService.obtenerFactorEmisionPorActividad(idActividad);

            if (factorEmision != null) {
                BigDecimal valorHuella = new BigDecimal(String.valueOf(huellaSeleccionada.getValor()));

                if (valorHuella.compareTo(BigDecimal.ZERO) <= 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Valor de huella incorrecto");
                    alert.setContentText("El valor de la huella no es válido para el cálculo.");
                    alert.showAndWait();
                    return;
                }

                BigDecimal huellaCarbono = valorHuella.multiply(factorEmision);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Huella de Carbono");
                alert.setHeaderText("La huella de carbono calculada es:");
                alert.setContentText("La huella de carbono para esta actividad es: " + huellaCarbono + " kg CO₂/" + huellaSeleccionada.getUnidad());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Factor de emisión no encontrado");
                alert.setContentText("No se ha encontrado el factor de emisión para la actividad seleccionada.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setHeaderText("Ninguna huella seleccionada");
            alert.setContentText("Por favor, selecciona una huella de la tabla.");
            alert.showAndWait();
        }
    }

    @FXML
    public void mostrarRecomendaciones() {
        Huella huellaSeleccionada = tableHuellas.getSelectionModel().getSelectedItem();

        if (huellaSeleccionada != null) {
            Actividad actividad = huellaSeleccionada.getIdActividad();
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
    @FXML
    public void switchToAnotherScene() {
        try {
            Scenes.setRoot("pantallaMenu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToInsertHuella() {
        try {
            Scenes.setRoot("pantallaInsertHuella");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}