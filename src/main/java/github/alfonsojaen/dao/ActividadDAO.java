package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Actividad;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ActividadDAO {

    private static final String QUERY_FIND_ALL_ACTIVIDAD = "FROM Actividad";
    private static final String QUERY_FIND_UNIDAD_BY_ACTIVIDAD = "SELECT c.unidad FROM Categoria c JOIN Actividad a ON a.idCategoria = c WHERE a.idCategoria.id = :idCategoria";

    public List<Actividad> allActividades() {
        Session session = Connection.getInstance().getSession();
        Query<Actividad> miQuery = session.createQuery(QUERY_FIND_ALL_ACTIVIDAD, Actividad.class);
        List<Actividad> actividades = miQuery.getResultList();
        session.close();
        return actividades;
    }

    public List<String> findUnidadByActividad(int idCategoria) {
        Session session = Connection.getInstance().getSession();
        Query<String> query = session.createQuery(QUERY_FIND_UNIDAD_BY_ACTIVIDAD, String.class);
        query.setParameter("idCategoria", idCategoria);
        List<String> unidades = query.getResultList();
        session.close();
        return unidades;
    }
}



