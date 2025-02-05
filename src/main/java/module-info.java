module github.alfonsojaen {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires kernel;
    requires layout;

    opens github.alfonsojaen to javafx.fxml;
    opens github.alfonsojaen.entities to org.hibernate.orm.core, javafx.base;


    exports github.alfonsojaen;
    exports github.alfonsojaen.view;
    opens github.alfonsojaen.view to javafx.fxml;

}
