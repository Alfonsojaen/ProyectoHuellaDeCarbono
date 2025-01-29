package github.alfonsojaen.services;

import github.alfonsojaen.dao.RecomendacionDAO;
import github.alfonsojaen.entities.Recomendacion;

import java.util.List;

public class RecomendacionService {

    private RecomendacionDAO recomendacionDAO = new RecomendacionDAO();

    public List<Recomendacion> findRecomendacionesByCategoria(Integer idCategoria) {
        if (idCategoria == null) {
            return null;
        }
        return recomendacionDAO.findRecomendacionesByCategoria(idCategoria);
    }
}
