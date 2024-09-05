module com.julioreigen.ope.onlineplayerextractor {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;

    opens com.julioreigen.ope.onlineplayerextractor to javafx.fxml;
    exports com.julioreigen.ope.onlineplayerextractor;
}