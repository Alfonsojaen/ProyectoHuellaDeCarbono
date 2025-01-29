package github.alfonsojaen.view;

import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

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

    private HuellaService huellaService;

    public ControllerShowHuellas() {
        huellaService = new HuellaService();
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
}

