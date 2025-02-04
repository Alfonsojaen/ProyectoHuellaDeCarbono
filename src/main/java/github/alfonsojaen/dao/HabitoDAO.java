package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Habito;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HabitoDAO {

    private static final String QUERY_FIND_ALL_HABITO = "FROM Habito";
    private static final String QUERY_EXISTE_HABITO = "FROM Habito h WHERE h.id.idActividad = :idActividad AND h.id.idUsuario = :idUsuario";

    public void createHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();
        session.save(habito);
        session.getTransaction().commit();
        session.close();
    }
    public List<Habito> allHabitos() {
        Session session = Connection.getInstance().getSession();
        Query<Habito> miQuery = session.createQuery(QUERY_FIND_ALL_HABITO, Habito.class);
        List<Habito> habitos = miQuery.getResultList();
        session.close();
        return habitos;
    }

    public boolean existeHabito(int idActividad, int idUsuario) {
        Session session = Connection.getInstance().getSession();
        Query<Habito> query = session.createQuery(QUERY_EXISTE_HABITO, Habito.class);
        query.setParameter("idActividad", idActividad);
        query.setParameter("idUsuario", idUsuario);
        boolean existe = !query.getResultList().isEmpty();
        session.close();
        return existe;
    }
    public void eliminarHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(habito);
        transaction.commit();
        session.close();
    }
    public void actualizarHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(habito);
        transaction.commit();
        session.close();
    }
}

