package github.alfonsojaen.services;

import github.alfonsojaen.dao.ActividadDAO;
import github.alfonsojaen.entities.Actividad;

import java.util.List;

public class ActividadService {

    ActividadDAO actividadDAO = new ActividadDAO();


    public List<Actividad> getAllActividades() {
        return actividadDAO.allActividades();
    }

    public List<String> findUnidadByActividad(int idCategoria) {
        return actividadDAO.findUnidadByActividad(idCategoria);
    }

}
