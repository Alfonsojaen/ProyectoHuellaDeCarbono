package github.alfonsojaen.view;

import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.services.UsuarioService;
import github.alfonsojaen.singleton.UserSession;
import github.alfonsojaen.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ControllerUpdateUser {
    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtPassword;

    private final UsuarioService usuarioService = new UsuarioService();
    private Usuario usuarioActual;

    @FXML
    public void initialize() {
        usuarioActual = UserSession.getInstancia().getUsuarioIniciado();

        if (usuarioActual != null) {
            txtNombre.setText(usuarioActual.getNombre());
            txtCorreo.setText(usuarioActual.getEmail());
            txtPassword.setText(usuarioActual.getContraseña());
        } else {
            Utils.Alert("Error", "Usuario no autenticado", "No se encontró un usuario en sesión.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void actualizarUsuario() {
        if (usuarioActual == null) {
            Utils.Alert("Error", "Usuario no encontrado", "No se encontró un usuario para actualizar.", Alert.AlertType.ERROR);
            return;
        }

        String nuevoNombre = txtNombre.getText().trim();
        String nuevoCorreo = txtCorreo.getText().trim();
        String nuevaPassword = txtPassword.getText().trim();

        if (nuevoNombre.isEmpty() || nuevoCorreo.isEmpty()) {
            Utils.Alert("Error", "Campos vacíos", "El nombre y el correo son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (!nuevoCorreo.matches(emailRegex)) {
            Utils.Alert("Error", "Formato de email inválido", "Por favor, ingrese un email válido.", Alert.AlertType.ERROR);
            return;
        }

        usuarioActual.setNombre(nuevoNombre);
        usuarioActual.setEmail(nuevoCorreo);

        if (!nuevaPassword.isEmpty()) {
            usuarioActual.setContraseña(Utils.encryptSHA256(nuevaPassword));
        }

        boolean actualizado = usuarioService.actualizarUsuario(usuarioActual);

        if (actualizado) {
            Utils.Alert("Éxito", "Usuario actualizado", "Los datos han sido actualizados correctamente.", Alert.AlertType.INFORMATION);
            UserSession.getInstancia().logIn(usuarioActual);
        } else {
            Utils.Alert("Error", "Fallo en la actualización", "No se pudo actualizar el usuario. Inténtalo de nuevo.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void switchToMenu() throws IOException {
        Scenes.setRoot("pantallaMenu");
    }
}