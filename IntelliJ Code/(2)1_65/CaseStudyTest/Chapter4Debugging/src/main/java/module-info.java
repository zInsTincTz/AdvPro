module chapter4.chapter4debugging {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires org.apache.logging.log4j;


    opens chapter4debugging to javafx.fxml;
    exports chapter4debugging;
}