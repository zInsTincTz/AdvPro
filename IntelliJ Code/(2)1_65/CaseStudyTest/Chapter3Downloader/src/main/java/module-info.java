module chapter3downloader{
    requires javafx.controls;
    requires javafx.fxml;
    requires pdfbox.app;

    opens chapter3downloader to javafx.fxml;
    opens chapter3downloader.controller to javafx.fxml;
    exports chapter3downloader;
    exports chapter3downloader.controller;
}