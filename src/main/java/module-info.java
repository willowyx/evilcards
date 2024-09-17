module dev.willowyx.evilcards {
    requires javafx.fxml;
    requires javafx.controls;

    requires org.controlsfx.controls;
    requires com.almasb.fxgl.all;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.uuid;
    requires atlantafx.base;

    opens dev.willowyx.evilcards to javafx.fxml;
    exports dev.willowyx.evilcards;
}