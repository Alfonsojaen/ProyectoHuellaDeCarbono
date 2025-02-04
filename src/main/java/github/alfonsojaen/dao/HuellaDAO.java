package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Huella;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HuellaDAO {

    private static final String QUERY_FIND_ALL_HUELLA = "FROM Huella";

    public static void crearHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(huella);
        transaction.commit();
        session.close();
    }

    public List<Huella> allHuellas() {
        Session session = Connection.getInstance().getSession();
        Query<Huella> miQuery = session.createQuery(QUERY_FIND_ALL_HUELLA, Huella.class);
        List<Huella> huellas = miQuery.getResultList();
        session.close();
        return huellas;
    }

    public void actualizarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(huella);
        transaction.commit();
        session.close();
    }

    public void eliminarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.delete(huella);
        transaction.commit();
        session.close();
    }

}

