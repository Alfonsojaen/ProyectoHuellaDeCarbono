module github.alfonsojaen {
    requires java.naming;
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    opens github.alfonsojaen to javafx.fxml;
    opens github.alfonsojaen.entities to org.hibernate.orm.core;

    exports github.alfonsojaen;

}
