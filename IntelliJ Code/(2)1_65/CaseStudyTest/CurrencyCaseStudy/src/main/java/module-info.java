module CurrencyStudy{
    requires javafx.controls;
    requires javafx.fxml;

    requires org.json;
    requires org.apache.commons.io;

    opens CurrencyStudy to javafx.fxml;
    exports CurrencyStudy;
}