package github.alfonsojaen.test;

import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.UsuarioService;

public class UsuarioServiceTest {
    public static void main(String[] args) {
        UsuarioService usuarioService = new UsuarioService();

        Usuario usuarioValido = new Usuario();
        usuarioValido.setEmail("alfonso");
        usuarioValido.setContraseña("alfonso");
        boolean resultadoValido = usuarioService.login(usuarioValido);
        System.out.println("Caso 1: Usuario válido -> " + (resultadoValido ? "PASÓ" : "FALLÓ"));

        Usuario usuarioEmailNulo = new Usuario();
        usuarioEmailNulo.setEmail(null);
        usuarioEmailNulo.setContraseña("alfonso");
        boolean resultadoEmailNulo = usuarioService.login(usuarioEmailNulo);
        System.out.println("Caso 2: Email nulo -> " + (resultadoEmailNulo ? "PASÓ" : "FALLÓ"));

        Usuario usuarioContrasenaNula = new Usuario();
        usuarioContrasenaNula.setEmail("alfonsoaaq");
        usuarioContrasenaNula.setContraseña(null);
        boolean resultadoContrasenaNula = usuarioService.login(usuarioContrasenaNula);
        System.out.println("Caso 3: Contraseña nula -> " + (resultadoContrasenaNula ? "PASÓ" : "FALLÓ"));

        Usuario usuarioNoRegistrado = new Usuario();
        usuarioNoRegistrado.setEmail("alfonso");
        usuarioNoRegistrado.setContraseña("alfonso");
        boolean resultadoNoRegistrado = usuarioService.login(usuarioNoRegistrado);
        System.out.println("Caso 4: Usuario no registrado -> " + (resultadoNoRegistrado ? "PASÓ" : "FALLÓ"));
    }
}
