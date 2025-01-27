package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.vista.Vista;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDAO {
    public static void main(String[] args) {
        crearUsuario();
    }
    private static void crearUsuario() {
        Session session = Connection.getInstance().getSession();
        Usuario user = Vista.crearUsuario();
        Transaction transaction = session.beginTransaction();
        session.save(user);
        transaction.commit();
        session.close();

    }
    private static void actualizarUsuario() {}
}
