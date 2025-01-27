package github.alfonsojaen.dao;

import github.alfonsojaen.connection.Connection;
import github.alfonsojaen.entities.Actividad;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.vista.Vista;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HuellaDAO {

    private static void crearHuella() {
        Session session = Connection.getInstance().getSession();
        Huella huella = Vista.pideHuella();
        Transaction transaction = session.beginTransaction();
        session.save(huella);
        transaction.commit();
        System.out.println("Insertada huella: " + huella);
        session.close();
    }
}

