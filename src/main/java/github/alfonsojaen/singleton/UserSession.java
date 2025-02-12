package github.alfonsojaen.singleton;


import github.alfonsojaen.entities.Usuario;

public class UserSession {
    private static UserSession _instance;
    private static Usuario userLoged;

    private UserSession() {
    }

    public static UserSession getInstancia() {
        if (_instance == null) {
            _instance = new UserSession();
            _instance.logIn(userLoged);
        }
        return _instance;
    }

    public void logIn(Usuario user) {
        userLoged = user;
    }

    public Usuario getUsuarioIniciado() {
        return userLoged;
    }

    public void logOut() {
        userLoged = null;
    }
}

