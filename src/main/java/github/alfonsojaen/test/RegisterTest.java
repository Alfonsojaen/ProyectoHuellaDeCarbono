package github.alfonsojaen.test;

import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.UsuarioService;

import java.time.Instant;
import java.util.Scanner;

public class RegisterTest {
    public static void main(String[] args) {
            Scanner scanner = new Scanner(System.in);

        // Crear una instancia del servicio
        UsuarioService usuarioService = new UsuarioService();

        // Solicitar al usuario ingresar los datos
        System.out.print("Introduce tu nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Introduce tu email: ");
        String email = scanner.nextLine();

        System.out.print("Introduce tu contraseña: ");
        String contrasena = scanner.nextLine();

        // Crear el objeto Usuario con los datos proporcionados
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setContraseña(contrasena);

        // Establecer la fecha de registro automáticamente
        Instant fechaActual = Instant.now();
        usuario.setFechaRegistro(fechaActual);
        // Intentar registrar al usuario
        boolean registroExitoso = usuarioService.register(usuario);

        // Mostrar el resultado del registro
        if (registroExitoso) {
            System.out.println("¡Registro exitoso!");
        } else {
            System.out.println("Error: No se pudo registrar al usuario (posiblemente el email ya existe).");
        }

        scanner.close();
    }
}

