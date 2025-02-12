package github.alfonsojaen.services;

import github.alfonsojaen.dao.HabitoDAO;
import github.alfonsojaen.entities.Habito;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.singleton.UserSession;

import java.util.List;

public class HabitoService {

    HabitoDAO habitoDAO  = new HabitoDAO();

    public boolean createHabito(Habito habito) {
        if (habito.getFrecuencia() == null || habito.getTipo() == null || habito.getUltimaFecha() == null || habito.getIdActividad() == null) {
            return false;
        }
        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioLogueado == null) {
            return false;
        }
        habitoDAO.createHabito(habito);
        return true;
    }

    public List<Habito> getAllHabitos() {
        return habitoDAO.allHabitos();
    }

    public boolean existeHabito(int idActividad, int idUsuario) {
        return habitoDAO.existeHabito(idActividad, idUsuario);
    }

    public void actualizarHabito(Habito habito) {
        try {
            if (habito == null || habito.getId() == null) {
                return;
            }
            habitoDAO.actualizarHabito(habito);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean eliminarHabito(Habito habito) {
        try {
            if (habito == null || habito.getId() == null) {
                return false;
            }
            habitoDAO.eliminarHabito(habito);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
