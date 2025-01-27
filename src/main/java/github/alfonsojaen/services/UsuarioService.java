package github.alfonsojaen.services;

import github.alfonsojaen.dao.UserDAO;
import github.alfonsojaen.entities.Usuario;
import github.alfonsojaen.singleton.UserSession;

public class UsuarioService {

UserDAO userDAO = new UserDAO();

    public boolean login(Usuario user) {
        if (user.getEmail() == null || user.getContraseña() == null) {
            return false;
        }
        Usuario authenticatedUser = userDAO.authenticateUser(user.getEmail(), user.getContraseña());

        if (authenticatedUser != null) {
            UserSession.getInstancia().logIn(authenticatedUser);
            return true;
        }else{
            return false;
        }
    }
    public boolean register(Usuario user) {
        if (user.getEmail() == null || user.getContraseña() == null || user.getEmail().isEmpty() || user.getContraseña().isEmpty()) {
            return false;
        }
        Usuario usuarioExistente = userDAO.findByEmail(user.getEmail());
        if (usuarioExistente != null) {
            return false;
        }
        userDAO.saveUser(user);
        return true;
    }



}
