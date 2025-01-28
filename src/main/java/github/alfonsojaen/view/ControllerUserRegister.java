package github.alfonsojaen.view;

import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.UsuarioService;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.Instant;

public class ControllerUserRegister {

    @FXML
    private TextField tgmail;
    @FXML
    private TextField tname;
    @FXML
    private PasswordField tpassword;
    private UsuarioService usuarioService;

    public ControllerUserRegister() {
        usuarioService = new UsuarioService();
    }

    @FXML
    public void btRegistrar() {
        addUsuario();
    }

    @FXML
    private void addUsuario() {
        String gmail = tgmail.getText();
        String name = tname.getText();
        String password = tpassword.getText();

        if ( gmail.isEmpty() || name.isEmpty() || password.isEmpty()) {
            Utils.Alert("Error", "Campos vacíos", "Por favor, complete todos los campos.", Alert.AlertType.ERROR);
            return;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!gmail.matches(emailRegex)) {
            Utils.Alert("Error", "Formato de email inválido", "Por favor, ingrese un email válido.", Alert.AlertType.ERROR);
            return;
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(name);
        usuario.setEmail(gmail);
        usuario.setContraseña(Utils.encryptSHA256(password));
        Instant fechaActual = Instant.now();
        usuario.setFechaRegistro(fechaActual);

        boolean registroExitoso = usuarioService.register(usuario);

        if (registroExitoso) {
            Utils.ShowAlert("Registro exitoso, El usuario ha sido registrado correctamente.");
        } else {
            Utils.ShowAlert("Error de registro, El correo electrónico ya está en uso o hay un error con los datos.");
        }
    }


    @FXML
    private void switchToLoginUser() {
        try {
            Scenes.setRoot("pantallaLoginUser");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}