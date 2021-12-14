module ImaginationWriter {
    requires MaterialFX;
    requires java.desktop;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.base;
    requires org.slf4j;
    requires java.sql;
    requires java.persistence;
    requires org.jfxtras.styles.jmetro;
    requires org.hibernate.orm.core;
    requires org.hibernate.commons.annotations;
    requires fxribbon;

    opens com.IW.controllers to javafx.fxml, javafx.controls, javafx.graphics, javafx.media, javafx.base, MaterialFX;
    opens com.IW.model.objects to org.hibernate.orm.core, org.hibernate.commons.annotations;
    opens com.IW.model.dao to org.hibernate.orm.core, org.hibernate.commons.annotations;
    exports com.IW;
}