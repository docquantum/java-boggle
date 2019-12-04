module boggle {
    requires javafx.controls;
    requires javafx.fxml;

    opens edu.unl.cse.csce361.boggle.frontend to javafx.fxml;
    opens edu.unl.cse.csce361.boggle.logic to javafx.fxml;
    opens edu.unl.cse.csce361.boggle.backend to javafx.fxml;
    exports edu.unl.cse.csce361.boggle.frontend;
    exports edu.unl.cse.csce361.boggle.logic;
    exports edu.unl.cse.csce361.boggle.backend;
}
