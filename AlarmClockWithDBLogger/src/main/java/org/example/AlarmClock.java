package org.example;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AlarmClock extends Application {

    private final ClockLogic logic = new ClockLogic();
    private final Label labelTime = new Label();
    private final Label labelAlarm = new Label();
    private final Label labelMode = new Label("Mode: Clock");

    private final Button buttonH = new Button("H+");
    private final Button buttonM = new Button("M+");
    private final Button buttonHMinus = new Button("H-");
    private final Button buttonMMinus = new Button("M-");
    private final Button buttonA = new Button("A");

    private final AudioClip sound = new AudioClip(getClass().getResource("/pirate.wav").toExternalForm());
    private final Image backgroundImage = new Image(getClass().getResource("/pirate.jpg").toExternalForm());
    private final ImageView pirateBackground = new ImageView(backgroundImage);

    @Override
    public void start(Stage stage) {
        VBox content = new VBox(10);
        content.setAlignment(Pos.CENTER);

        VBox buttonsH = new VBox(10);
        buttonsH.getChildren().addAll(buttonH, buttonHMinus);
        VBox buttonsM = new VBox(10);
        buttonsM.getChildren().addAll(buttonM, buttonMMinus);
        HBox buttonBox = new HBox(10, buttonsH, buttonsM, buttonA);
        buttonBox.setAlignment(Pos.CENTER);

        content.getChildren().addAll(labelTime, labelAlarm, labelMode, buttonBox);

        labelTime.setStyle("-fx-font-size: 28px;");
        labelAlarm.setStyle("-fx-font-size: 20px;");
        labelMode.setStyle("-fx-font-size: 18px;");
        buttonH.setStyle("-fx-font-size: 16px; -fx-pref-width: 60px;");
        buttonM.setStyle("-fx-font-size: 16px; -fx-pref-width: 60px;");
        buttonHMinus.setStyle("-fx-font-size: 16px; -fx-pref-width: 60px;");
        buttonMMinus.setStyle("-fx-font-size: 16px; -fx-pref-width: 60px;");
        buttonA.setStyle("-fx-font-size: 16px; -fx-pref-width: 60px;");

        pirateBackground.setFitWidth(400);
        pirateBackground.setFitHeight(300);
        pirateBackground.setPreserveRatio(false);
        pirateBackground.setVisible(false);

        StackPane root = new StackPane(pirateBackground, content);

        buttonH.setOnAction(e -> {
            logic.increaseAlarmHour();
            updateDisplay();
        });

        buttonM.setOnAction(e -> {
            logic.increaseAlarmMinute();
            updateDisplay();
        });

        buttonHMinus.setOnAction(e -> {
            logic.decreaseAlarmHour();
            updateDisplay();
        });

        buttonMMinus.setOnAction(e -> {
            logic.decreaseAlarmMinute();
            updateDisplay();
        });

        buttonA.setOnAction(e -> {
            logic.toggleAlarmMode();
            updateDisplay();
        });

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            logic.getCurrentTime();
            if (logic.checkAlarm()) {
                pirateBackground.setVisible(true);
                sound.play();
                showAlert("Alarm!", "It's time for pirates, Yo-ho-ho!");
                DBLogger.log("ALARM", "Alarm is up");
            }
            updateDisplay();
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();

        updateDisplay();

        Scene scene = new Scene(root, 400, 300);
        stage.setTitle("Alarm Clock");
        stage.getIcons().add(new Image(getClass().getResource("/TimeSaving.ico").toExternalForm()));
        stage.setScene(scene);
        stage.show();
    }

    private void updateDisplay() {
        labelTime.setText("Current: " + logic.getCurrentTime());

        if (logic.isAlarmOn()) {
            labelAlarm.setText("Alarm: " + logic.getAlarmTime());
            labelMode.setText("Mode: Alarm");
            labelAlarm.setVisible(true);
            buttonH.setDisable(false);
            buttonM.setDisable(false);
            buttonHMinus.setDisable(false);
            buttonMMinus.setDisable(false);
        } else {
            labelAlarm.setText("");
            labelMode.setText("Mode: Clock");
            labelAlarm.setVisible(false);
            buttonH.setDisable(true);
            buttonM.setDisable(true);
            buttonHMinus.setDisable(true);
            buttonMMinus.setDisable(true);
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.setTitle(title);

        alert.setOnHidden(e -> {
            logic.setAlarmMode(false);
            pirateBackground.setVisible(false);
            sound.stop();
            DBLogger.log("STATE_CHANGE","Alarm mode was deactivate");
            updateDisplay();
        });

        alert.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
