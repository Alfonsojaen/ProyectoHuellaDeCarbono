package github.alfonsojaen.view;

import github.alfonsojaen.entities.Actividad;
import github.alfonsojaen.entities.Habito;
import github.alfonsojaen.entities.HabitoId;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.ActividadService;
import github.alfonsojaen.services.HabitoService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.List;

public class ControllerInsertHabito {

    @FXML
    private TextField tFrecuencia;
    @FXML
    private ComboBox<Actividad> cActividad;
    @FXML
    private ComboBox<String> cTipoHabito;
    @FXML
    private DatePicker dFecha;

    private ActividadService actividadService;
    private HabitoService habitoService;

    public ControllerInsertHabito() {
        habitoService = new HabitoService();
        actividadService = new ActividadService();
    }

    @FXML
    public void initialize() {
        List<Actividad> actividadesDisponibles = actividadService.getAllActividades();

        if (actividadesDisponibles == null || actividadesDisponibles.isEmpty()) {
            System.out.println("⚠️ No hay actividades disponibles.");
        } else {
            cActividad.getItems().addAll(actividadesDisponibles);
        }

        cTipoHabito.getItems().addAll("Diario", "Semanal", "Mensual", "Anual");

        cActividad.setOnAction(event -> {
            Actividad seleccionada = cActividad.getValue();
        });
    }

    @FXML
    private void addHabito() {
        String frecuencia = tFrecuencia.getText();
        Actividad actividadSeleccionada = cActividad.getValue();
        String tipoHabito = cTipoHabito.getValue();
        LocalDate fechaSeleccionada = dFecha.getValue();


        if (frecuencia.isEmpty() || actividadSeleccionada == null || tipoHabito == null || fechaSeleccionada == null) {
            Utils.ShowAlert("Error, Campos vacíos, Por favor, complete todos los campos.");
            return;
        }

        if (!frecuencia.matches("\\d+")) {
            Utils.ShowAlert("Error, Frecuencia inválida, Ingrese un número válido para la frecuencia.");
            return;
        }

        if (fechaSeleccionada.isAfter(LocalDate.now())) {
            Utils.ShowAlert("Error, Fecha inválida, La fecha no puede ser posterior al día de hoy.");
            return;
        }

        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioLogueado == null) {
            Utils.ShowAlert("Error, Usuario no iniciado, Debe iniciar sesión para crear un hábito.");
            return;
        }

        Habito habito = new Habito();
        habito.setIdUsuario(usuarioLogueado);
        habito.setIdActividad(actividadSeleccionada);
        habito.setTipo(tipoHabito);
        habito.setFrecuencia(Integer.parseInt(frecuencia));
        habito.setUltimaFecha(fechaSeleccionada);
        HabitoId habitoId = new HabitoId();
        habitoId.setIdActividad(actividadSeleccionada.getId());
        habitoId.setIdUsuario(usuarioLogueado.getId());
        habito.setId(habitoId);


        boolean creado = habitoService.createHabito(habito);

        if (creado) {
            Utils.ShowAlert("Registro exitoso, el hábito ha sido creado correctamente.");
        } else {
            Utils.ShowAlert("Error de registro, el hábito no pudo ser creado. Verifique los datos e intente nuevamente.");
        }
    }
}