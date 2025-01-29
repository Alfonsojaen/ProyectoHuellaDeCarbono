package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Recomendacion;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class RecomendacionDAO {

    private static final String QUERY_FIND_RECOMENDACIONES_BY_CATEGORIA = "SELECT r FROM Recomendacion r WHERE r.idCategoria.id = :idCategoria";

    public List<Recomendacion> findRecomendacionesByCategoria(int idCategoria) {
        Session session = Connection.getInstance().getSession();
        Query<Recomendacion> query = session.createQuery(QUERY_FIND_RECOMENDACIONES_BY_CATEGORIA, Recomendacion.class);
        query.setParameter("idCategoria", idCategoria);
        List<Recomendacion> recomendaciones = query.getResultList();
        session.close();
        return recomendaciones;
    }
}
