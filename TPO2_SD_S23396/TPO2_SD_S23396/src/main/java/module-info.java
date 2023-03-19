module pjatk.tpo.tpo2_sd_s23396 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens pjatk.tpo.tpo2_sd_s23396 to javafx.fxml;
    exports pjatk.tpo.tpo2_sd_s23396;
}