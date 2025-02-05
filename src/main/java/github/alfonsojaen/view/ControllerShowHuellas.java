package github.alfonsojaen.view;



import github.alfonsojaen.entities.*;
import github.alfonsojaen.services.ActividadService;
import github.alfonsojaen.services.HuellaService;
import github.alfonsojaen.services.RecomendacionService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.BigDecimalStringConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;


public class ControllerShowHuellas {
    @FXML
    private TableView<Huella> tableHuellas;

    @FXML
    private TableColumn<Huella, Integer> colIdActividad;

    @FXML
    private TableColumn<Huella, BigDecimal> colValor;

    @FXML
    private TableColumn<Huella, String> colUnidad;

    @FXML
    private TableColumn<Huella, String> colFecha;


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
        tableHuellas.setEditable(true);

        colIdActividad.setCellValueFactory(new PropertyValueFactory<>("idActividad"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colUnidad.setCellValueFactory(new PropertyValueFactory<>("unidad"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));

        colValor.setCellFactory(column -> {
            TextFieldTableCell<Huella, BigDecimal> cell = new TextFieldTableCell<>(new BigDecimalStringConverter()) {
                @Override
                public void startEdit() {
                    super.startEdit();
                    TextField textField = (TextField) getGraphic();
                    if (textField != null) {
                        textField.setTextFormatter(new TextFormatter<>(change -> {
                            return change.getControlNewText().matches("\\d*\\.?\\d*") ? change : null;
                        }));
                    }
                }
            };
            return cell;
        });

        colValor.setOnEditCommit(event -> {
            Huella huella = event.getRowValue();
            try {
                BigDecimal nuevoValor = new BigDecimal(event.getNewValue().toString());
                huella.setValor(nuevoValor);
                huellaService.actualizarHuella(huella);

                try {
                    switchToShowHuellas();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (NumberFormatException e) {
                Utils.mostrarMensaje("Error", "Ingrese un número válido en la columna Valor.");
            }
            tableHuellas.refresh();
        });

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
                    Utils.mostrarMensaje("Error", "El valor de la huella no es válido para el cálculo.");
                    return;
                }

                BigDecimal huellaCarbono = valorHuella.multiply(factorEmision);
                Utils.mostrarMensaje("Huella de Carbono", "La huella de carbono para esta actividad es: " + huellaCarbono + " kg CO₂/" + huellaSeleccionada.getUnidad());

            } else {
                Utils.mostrarMensaje("Error", "No se encontró el factor de emisión para la actividad seleccionada.");

            }
        } else {
            Utils.mostrarMensaje("Advertencia", "Por favor, selecciona una huella de la tabla.");

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
                Utils.mostrarMensaje("Sin Recomendaciones", "No se han encontrado recomendaciones para la actividad seleccionada.");

            }
        } else {
            Utils.mostrarMensaje("Advertencia", "Por favor, selecciona un hábito de la tabla.");

        }
    }

    @FXML
    public void seguimientoHistorico() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Huella> listaHuellas = huellaService.getAllHuellas();
            List<Huella> listaFiltrada = listaHuellas.stream()
                    .filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());

            if (listaFiltrada.isEmpty()) {
                Utils.mostrarMensaje("Sin Datos", "No hay registros de huella para este usuario.");
                return;
            }

            Map<Integer, BigDecimal> huellaPorAnio = new HashMap<>();
            Map<String, BigDecimal> huellaPorMes = new HashMap<>();
            Map<String, BigDecimal> huellaPorSemana = new HashMap<>();

            for (Huella huella : listaFiltrada) {
                LocalDate fecha = huella.getFecha();
                if (fecha == null) continue;

                BigDecimal valorHuella = huella.getValor();
                if (valorHuella == null) continue;

                int anio = fecha.getYear();
                String mes = fecha.getYear() + "-" + String.format("%02d", fecha.getMonthValue());

                LocalDate inicioSemana = fecha.with(java.time.temporal.WeekFields.of(Locale.getDefault()).dayOfWeek(), 1);
                LocalDate finSemana = inicioSemana.plusDays(6);

                String semana = inicioSemana + " al " + finSemana;

                huellaPorAnio.put(anio, huellaPorAnio.getOrDefault(anio, BigDecimal.ZERO).add(valorHuella));
                huellaPorMes.put(mes, huellaPorMes.getOrDefault(mes, BigDecimal.ZERO).add(valorHuella));
                huellaPorSemana.put(semana, huellaPorSemana.getOrDefault(semana, BigDecimal.ZERO).add(valorHuella));
            }

            StringBuilder mensaje = new StringBuilder("\uD83D\uDCCA Seguimiento Histórico de Huella Ambiental:\n\n");

            mensaje.append("\uD83D\uDCC5 **Huella por Año:**\n");
            huellaPorAnio.forEach((anio, valor) -> mensaje.append("Año ").append(anio).append(": ").append(valor).append(" unidades\n"));

            mensaje.append("\n\uD83D\uDCC6 **Huella por Mes:**\n");
            huellaPorMes.forEach((mes, valor) -> mensaje.append("Mes ").append(mes).append(": ").append(valor).append(" unidades\n"));

            mensaje.append("\n\uD83D\uDCC2 **Huella por Semana:**\n");
            huellaPorSemana.forEach((semana, valor) ->
                    mensaje.append(semana).append(" = ( ").append(valor).append(" unidades )\n")
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Seguimiento Histórico");
            alert.setHeaderText("Comparación del impacto ambiental a lo largo del tiempo");
            alert.setContentText(mensaje.toString());

            ButtonType btnExportarPDF = new ButtonType("Exportar a PDF", ButtonBar.ButtonData.OK_DONE);
            ButtonType btnCerrar = new ButtonType("Cerrar", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(btnExportarPDF, btnCerrar);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == btnExportarPDF) {
                exportarPDF(mensaje.toString());
            }
        }
    }

    private void exportarPDF(String contenido) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos PDF", "*.pdf"));
        fileChooser.setInitialFileName("Seguimiento_Huella.pdf");

        File archivo = fileChooser.showSaveDialog(null);
        if (archivo != null) {
            try {
                PdfWriter writer = new PdfWriter(new FileOutputStream(archivo));
                PdfDocument pdfDoc = new PdfDocument(writer);
                Document document = new Document(pdfDoc);

                document.add(new Paragraph(contenido));
                document.close();

                Utils.mostrarMensaje("Éxito", "El PDF se ha generado correctamente en: " + archivo.getAbsolutePath());
            } catch (Exception e) {
                Utils.mostrarMensaje("Error", "No se pudo generar el PDF: " + e.getMessage());
            }
        }
    }
    //FILTROS

    @FXML
    public void abrirFiltros() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Filtrar Huellas");

        ComboBox<String> comboActividad = new ComboBox<>();
        List<String> actividades = huellaService.getAllHuellas().stream()
                .map(h -> h.getIdActividad().getNombre().trim())
                .distinct()
                .collect(Collectors.toList());
        comboActividad.getItems().addAll(actividades);
        comboActividad.setPromptText("Selecciona una actividad");

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Selecciona una fecha");

        VBox vbox = new VBox(10, new Label("Filtrar por actividad:"), comboActividad,
                new Label("Filtrar por fecha:"), datePicker);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(10));

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String actividadSeleccionada = comboActividad.getValue();
                LocalDate fechaSeleccionada = datePicker.getValue();
                aplicarFiltros(actividadSeleccionada, fechaSeleccionada);
            }
        });
    }


    private void aplicarFiltros(String actividad, LocalDate fecha) {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Huella> listaHuellas = huellaService.getAllHuellas().stream().filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId())).collect(Collectors.toList());

            List<Huella> listaFiltrada = listaHuellas.stream()
                    .filter(h -> actividad == null || actividad.isEmpty() || h.getIdActividad().getNombre().equals(actividad))
                    .filter(h -> fecha == null || h.getFecha() != null && h.getFecha().equals(fecha))
                    .collect(Collectors.toList());

            ObservableList<Huella> huellas = FXCollections.observableArrayList(listaFiltrada);
            tableHuellas.setItems(huellas);

        }
    }



    @FXML
    public void restablecerFiltros() {
        Usuario usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            List<Huella> listaHuellas = huellaService.getAllHuellas().stream()
                    .filter(h -> h.getIdUsuarioInt().equals(usuarioActual.getId()))
                    .collect(Collectors.toList());

            ObservableList<Huella> huellas = FXCollections.observableArrayList(listaHuellas);
            tableHuellas.setItems(huellas);
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
    private void switchToShowHuellas() throws IOException {
        Scenes.setRoot("pantallaHuellas");
    }
    @FXML
    private void switchToDeleteHuella() throws IOException {
        Scenes.setRoot("pantallaDeleteHuella");
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
