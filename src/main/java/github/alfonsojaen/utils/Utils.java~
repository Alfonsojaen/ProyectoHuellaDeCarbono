package github.alfonsojaen.utils;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.security.MessageDigest;

public class Utils {
    public static String encryptSHA256 (String s){
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA256");

            md.update(s.getBytes()); 	// Se actualiza el MessageDigest con los bytes de la cadena de entrada

            StringBuilder sb = new StringBuilder();
            for (byte aByte : md.digest()) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;  // Se devuelve el resultado del hash en formato hexadecimal
    }

    public static Alert Alert(String title, String header, String text, Alert.AlertType type) {
        Alert alertDialog = new Alert(type);
        alertDialog.setTitle(title);
        alertDialog.setHeaderText(header);
        alertDialog.setContentText(text);

        // Mostrar el cuadro de diálogo de manera no bloqueante
        alertDialog.show();

        Stage s = (Stage) alertDialog.getDialogPane().getScene().getWindow();
        s.toFront();

        return alertDialog;
    }


    public static void ShowAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }
}

