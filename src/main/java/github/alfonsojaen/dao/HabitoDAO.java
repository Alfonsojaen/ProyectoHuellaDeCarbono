package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Habito;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HabitoDAO {
    private static final String QUERY_FIND_ALL_HABITOS = "FROM Habito";
    private static final String QUERY_FIND_HABITOS_BY_USUARIO = "FROM Habito WHERE idUsuario = :idUsuario";
    private static final String QUERY_FIND_HABITOS_BY_ACTIVIDAD = "FROM Habito WHERE idActividad = :idActividad";
    private static final String QUERY_FIND_HABITOS_BY_FRECUENCIA = "FROM Habito WHERE frecuencia = :frecuencia";
    private static final String QUERY_FIND_HABITOS_BY_TIPO = "FROM Habito WHERE tipo = :tipo";

    // Método para obtener todos los hábitos
    public List<Habito> allHabitos() {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_FIND_ALL_HABITOS, Habito.class);
        List<Habito> habitos = query.getResultList();
        session.close();
        return habitos;
    }

    // Método para obtener hábitos por usuario
    public List<Habito> findHabitosByUsuario(int idUsuario) {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_FIND_HABITOS_BY_USUARIO, Habito.class);
        query.setParameter("idUsuario", idUsuario);
        List<Habito> habitos = query.getResultList();
        session.close();
        return habitos;
    }

    // Método para obtener hábitos por actividad
    public List<Habito> findHabitosByActividad(int idActividad) {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_FIND_HABITOS_BY_ACTIVIDAD, Habito.class);
        query.setParameter("idActividad", idActividad);
        List<Habito> habitos = query.getResultList();
        session.close();
        return habitos;
    }

    // Método para obtener hábitos por frecuencia
    public List<Habito> findHabitosByFrecuencia(int frecuencia) {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_FIND_HABITOS_BY_FRECUENCIA, Habito.class);
        query.setParameter("frecuencia", frecuencia);
        List<Habito> habitos = query.getResultList();
        session.close();
        return habitos;
    }

    // Método para obtener hábitos por tipo (diaria, semanal, mensual, anual)
    public List<Habito> findHabitosByTipo(String tipo) {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_FIND_HABITOS_BY_TIPO, Habito.class);
        query.setParameter("tipo", tipo);
        List<Habito> habitos = query.getResultList();
        session.close();
        return habitos;
    }

    // Método para agregar un nuevo hábito
    public void createHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        session.save(habito);
        session.getTransaction().commit();
        session.close();
    }

    // Método para actualizar un hábito existente
    public void updateHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        session.merge(habito);
        session.getTransaction().commit();
        session.close();
    }

    // Método para eliminar un hábito
    public void deleteHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        session.remove(habito);
        session.getTransaction().commit();
        session.close();
    }
}

