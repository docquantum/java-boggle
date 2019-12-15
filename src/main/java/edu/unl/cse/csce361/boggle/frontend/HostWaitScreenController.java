package edu.unl.cse.csce361.boggle.frontend;

import edu.unl.cse.csce361.boggle.backend.BackendManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HostWaitScreenController implements Initializable {
    @FXML
    private Label IPAddress;
    @FXML
    private Label portNumber;
    @FXML
    private Button startGameButt;
    @FXML
    private Label waitingLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        IPAddress.setText(BackendManager.getInstance().getAddress());
        portNumber.setText(String.valueOf(BackendManager.getInstance().getPort()));
        BackendManager.getInstance().getAllReadyProperty().addListener((observableValue, oldBool, newBool) -> {
            if (newBool) {
                waitingLabel.setVisible(false);
                startGameButt.setVisible(true);
            }
        });
    }

    public void startGame(Event event) throws IOException {
        BackendManager.getInstance().hostStartGame();
        ScreenController.getInstance().switchScreen(event, "FXML/BoggleScreen.fxml");
    }
}
