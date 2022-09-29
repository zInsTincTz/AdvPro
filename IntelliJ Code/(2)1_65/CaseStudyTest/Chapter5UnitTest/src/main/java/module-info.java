module chapter4.chapter4debugging {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.apache.logging.log4j;
    requires javafx.swing;
    //requires org.junit.jupiter;
    //requires org.junit.platform;

    opens chapter4debugging to javafx.fxml;
    exports chapter4debugging;
    exports chapter4debugging.controller;
    opens chapter4debugging.controller to javafx.fxml;
}