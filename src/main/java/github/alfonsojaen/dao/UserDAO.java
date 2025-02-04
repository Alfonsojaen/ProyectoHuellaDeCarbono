package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Usuario;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;


public class UserDAO {


    private static final String QUERY_AUTENTICAR_USUARIO = "FROM Usuario WHERE email = :email AND contraseña = :contraseña";
    private static final String QUERY_BUSCAR_USUARIO = "FROM Usuario u WHERE u.email = :email";


    public static void saveUser(Usuario user) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();

    }

    public static void updateUser(Usuario user) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = session.beginTransaction();
        session.merge(user);
        transaction.commit();
        session.close();
    }

    public Usuario authenticateUser(String email, String contraseña) {
        Session session = Connection.getInstance().getSession();
        try {
            Query query = session.createQuery(QUERY_AUTENTICAR_USUARIO);
            query.setParameter("email", email);
            query.setParameter("contraseña", contraseña);
            return (Usuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            session.close();
        }
    }


    public Usuario findByEmail(String email) {
        Session session = Connection.getInstance().getSession();
        try {
            Query query = session.createQuery(QUERY_BUSCAR_USUARIO, Usuario.class);
            query.setParameter("email", email);
            return (Usuario) query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            session.close();
        }
    }

}


