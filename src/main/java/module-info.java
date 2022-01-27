module com.elltechs._gui_oxy_rev02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires com.fazecast.jSerialComm;
    opens com.elltechs._gui_oxy_rev02 to javafx.fxml;
    exports com.elltechs._gui_oxy_rev02;
}
