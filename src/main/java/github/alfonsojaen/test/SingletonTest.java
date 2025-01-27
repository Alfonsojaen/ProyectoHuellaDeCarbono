package github.alfonsojaen.test;

import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.UsuarioService;
import github.alfonsojaen.singleton.UserSession;

public class SingletonTest {
    public static void main(String[] args) {
        // Creamos un usuario de ejemplo
        Usuario usuario1 = new Usuario();
        usuario1.setEmail("j");
        usuario1.setContraseña("j");

        // Creamos una instancia de UsuarioService
        UsuarioService usuarioService = new UsuarioService();

        // Intentamos hacer login con las credenciales del usuario
        if (usuarioService.login(usuario1)) {
            System.out.println("Login exitoso");

            // Verificamos si el usuario está guardado en la sesión
            Usuario usuarioEnSesion = UserSession.getInstancia().getUsuarioIniciado();
            System.out.println("Usuario en sesión: " + usuarioEnSesion.getEmail());
        } else {
            System.out.println("Fallo en el login");
        }
    }
    }
