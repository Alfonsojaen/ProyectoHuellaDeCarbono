package github.alfonsojaen.services;

import github.alfonsojaen.dao.HuellaDAO;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.singleton.UserSession;

public class HuellaService {

    HuellaDAO userDAO = new HuellaDAO();

    public boolean crearHuella(Huella huella) {
        if ( huella.getId() == null || huella.getValor() == null || huella.getUnidad() == null || huella.getFecha() == null || huella.getIdActividad() == null) {
            return false;
        }
        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioLogueado == null) {
            return false;
        }

        Huella huellaExistente = userDAO.findById(huella.getId());
        if (huellaExistente != null) {
            return false;
        }
        huella.setIdUsuario(usuarioLogueado);
        userDAO.crearHuella(huella);
        return true;
    }
}
