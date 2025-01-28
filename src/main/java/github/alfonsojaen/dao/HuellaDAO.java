package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Huella;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HuellaDAO {

    public static void crearHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(huella);
        transaction.commit();
        session.close();
    }

    public Huella findById(Integer id) {
        Session session = Connection.getInstance().getSession();
        try {
            return session.get(Huella.class, id);
        } finally {
            session.close();
        }
    }
}

