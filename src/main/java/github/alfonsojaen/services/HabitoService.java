package github.alfonsojaen.services;

import github.alfonsojaen.dao.HabitoDAO;
import github.alfonsojaen.entities.Habito;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.singleton.UserSession;

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
}
