module CaseStudyTest {
    requires javafx.controls;
    requires javafx.fxml;


    opens CaseStudyTest to javafx.fxml;
    exports CaseStudyTest;
}