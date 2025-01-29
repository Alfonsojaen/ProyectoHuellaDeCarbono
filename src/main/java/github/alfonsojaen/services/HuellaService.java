package github.alfonsojaen.services;

import github.alfonsojaen.dao.HuellaDAO;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.singleton.UserSession;

import java.util.List;

public class HuellaService {

    HuellaDAO huellaDAO = new HuellaDAO();

    public boolean createHuella(Huella huella) {
        if (huella.getValor() == null || huella.getUnidad() == null || huella.getIdActividad() == null) {
            return false;
        }
        Usuario usuarioLogueado = UserSession.getInstancia().getUsuarioIniciado();
        if (usuarioLogueado == null) {
            return false;
        }
        huellaDAO.crearHuella(huella);
        return true;
    }

    public List<Huella> getAllHuellas() {
        return huellaDAO.allHuellas();
    }
}
