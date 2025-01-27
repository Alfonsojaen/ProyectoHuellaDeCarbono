package github.alfonsojaen.vista;

import github.alfonsojaen.entities.Actividad;
import github.alfonsojaen.entities.Huella;
import github.alfonsojaen.entities.Usuario;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Scanner;

public class Vista {
    public static Huella pideHuella() {
        Scanner scanner = new Scanner(System.in);

        // Pedir valor de la huella
        System.out.print("Introduce el valor de la huella: ");
        BigDecimal valor = scanner.nextBigDecimal();

        // Pedir unidad de la huella
        System.out.print("Introduce la unidad de la huella: ");
        String unidad = scanner.next();

        // Pedir fecha de la huella
        System.out.print("Introduce la fecha de la huella (YYYY-MM-DD): ");
        String fechaStr = scanner.next();
        LocalDate fecha = LocalDate.parse(fechaStr);

        // Pedir ID de la actividad asociada
        System.out.print("Introduce el ID de la actividad asociada: ");
        int actividadId = scanner.nextInt();

        // Crear la huella
        Huella huella = new Huella();
        huella.setValor(valor);
        huella.setUnidad(unidad);
        huella.setFecha(fecha);

        Actividad actividad = new Actividad();
        actividad.setId(actividadId);
        huella.setIdActividad(actividad);

        return huella;
    }

    public static Usuario crearUsuario() {
        System.out.println("Crear nuevo usuario");
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre de usuario: ");
        String username = scanner.nextLine();
        System.out.print("Introduce la contraseña: ");
        String password = scanner.nextLine();
        System.out.print("Introduce el correo electrónico: ");
        String email = scanner.nextLine();

        github.alfonsojaen.entities.Usuario usuario = new github.alfonsojaen.entities.Usuario();
        usuario.setNombre(username);
        usuario.setContraseña(password);
        usuario.setEmail(email);
        LocalDate fechaActual = LocalDate.now();
        Instant fechaRegistro = fechaActual.atStartOfDay(ZoneId.systemDefault()).toInstant();
        usuario.setFechaRegistro(fechaRegistro);


        System.out.println("Usuario creado: " + usuario);

        return usuario;
    }
}
