module org.vrp.vehicleroutingproblem {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.vrp.vehicleroutingproblem to javafx.fxml;
    exports org.vrp.vehicleroutingproblem;
}