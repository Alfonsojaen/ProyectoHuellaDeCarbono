package github.alfonsojaen.view;

import github.alfonsojaen.entities.*;
import github.alfonsojaen.services.HabitoService;
import github.alfonsojaen.services.RecomendacionService;
import github.alfonsojaen.singleton.UserSession;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.BigDecimalStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
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
        tableHabitos.setEditable(true);

        colIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        colFrecuencia.setCellValueFactory(new PropertyValueFactory<>("frecuencia"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUltimaFecha.setCellValueFactory(new PropertyValueFactory<>("ultimaFecha"));

        colFrecuencia.setCellFactory(column -> {
            TextFieldTableCell<Habito, Integer> cell = new TextFieldTableCell<>(new IntegerStringConverter()) {
                @Override
                public void startEdit() {
                    super.startEdit();
                    TextField textField = (TextField) getGraphic();
                    if (textField != null) {
                        textField.setTextFormatter(new TextFormatter<>(change ->
                                change.getControlNewText().matches("\\d*") ? change : null
                        ));
                    }
                }
            };
            return cell;
        });

        colFrecuencia.setOnEditCommit(event -> {
            Habito habito = event.getRowValue();
            try {
                Integer nuevaFrecuencia = event.getNewValue();
                if (nuevaFrecuencia != null) {
                    habito.setFrecuencia(nuevaFrecuencia);
                    habitoService.actualizarHabito(habito);
                }
            } catch (NumberFormatException e) {
                System.err.println("Error: Ingrese solo números para la frecuencia.");
            }
            tableHabitos.refresh();
        });

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

    @FXML
    public void abrirFiltros() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Filtrar Hábitos");

        ComboBox<String> comboActividad = new ComboBox<>();
        List<String> actividades = habitoService.getAllHabitos().stream()
                .map(h -> h.getIdActividad().getNombre().trim())
                .distinct()
                .collect(Collectors.toList());
        comboActividad.getItems().addAll(actividades);
        comboActividad.setPromptText("Selecciona un hábito");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Selecciona una fecha");

        ComboBox<String> comboTipo = new ComboBox<>();
        comboTipo.getItems().addAll("Diario", "Semanal", "Mensual", "Anual");
        comboTipo.setPromptText("Selecciona un tipo");

        VBox vbox = new VBox(10,
                new Label("Filtrar por actividad:"), comboActividad,
                new Label("Filtrar por fecha:"), datePicker,
                new Label("Filtrar por tipo:"), comboTipo);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String actividadSeleccionada = comboActividad.getValue();
                LocalDate fechaSeleccionada = datePicker.getValue();
                String tipoSeleccionado = comboTipo.getValue();
                aplicarFiltros(actividadSeleccionada, fechaSeleccionada, tipoSeleccionado);
            }
        });
    }

    private void aplicarFiltros(String actividad, LocalDate fecha, String tipo) {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Habito> listaHabitos = habitoService.getAllHabitos().stream()
                    .filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());

            List<Habito> listaFiltrada = listaHabitos.stream()
                    .filter(h -> actividad == null || actividad.isEmpty() || h.getIdActividad().getNombre().equals(actividad))
                    .filter(h -> fecha == null || (h.getUltimaFecha() != null && h.getUltimaFecha().equals(fecha)))
                    .filter(h -> tipo == null || tipo.isEmpty() || h.getTipo().equals(tipo))
                    .collect(Collectors.toList());

            ObservableList<Habito> habitos = FXCollections.observableArrayList(listaFiltrada);
            tableHabitos.setItems(habitos);
        }
    }

    @FXML
    public void restablecerFiltros() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Habito> listaHabitos = habitoService.getAllHabitos().stream()
                    .filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());

            ObservableList<Habito> habitos = FXCollections.observableArrayList(listaHabitos);
            tableHabitos.setItems(habitos);
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
    private void switchToShowHabitos() throws IOException {
        Scenes.setRoot("pantallaHabitos");
    }
    @FXML
    public void switchToInsertHabito() {
        try {
            Scenes.setRoot("pantallaInsertHabito");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void switchToDeleteHabito() {
        try {
            Scenes.setRoot("pantallaDeleteHabito");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}