module thenexus.demo2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.slf4j;
    requires java.naming;
    requires java.desktop;
    requires kernel;
    requires layout;
    requires io; // Needed for java.awt.Desktop to open PDF

    // DO NOT include iText modules here — they are not JPMS modules

    opens thenexus.demo2 to javafx.fxml;
    opens thenexus.demo2.model to org.hibernate.orm.core, jakarta.persistence, javafx.base;
    opens thenexus.demo2.dao to jakarta.persistence, javafx.base, org.hibernate.orm.core;

    exports thenexus.demo2;
}
