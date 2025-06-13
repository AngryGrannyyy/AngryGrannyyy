package org.example.Procedure;

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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ProcedureClock extends Application {

    static int alarmHour = 0;
    static int alarmMinute = 0;
    static boolean alarmMode = false;

    static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    static Label labelTime = new Label();
    static Label labelAlarm = new Label();
    static Label labelMode = new Label("Mode: Clock");
    static Button buttonH = new Button("H+");
    static Button buttonM = new Button("M+");
    static Button buttonHMinus = new Button("H-");
    static Button buttonMMinus = new Button("M-");
    static Button buttonA = new Button("A");

    static AudioClip sound;
    static ImageView pirateBackground;

    @Override
    public void start(Stage stage) {
        sound = new AudioClip(getClass().getResource("/pirate.wav").toExternalForm());
        Image backgroundImage = new Image(getClass().getResource("/pirate.jpg").toExternalForm());
        pirateBackground = new ImageView(backgroundImage);
        pirateBackground.setFitWidth(400);
        pirateBackground.setFitHeight(300);
        pirateBackground.setPreserveRatio(false);
        pirateBackground.setVisible(false);

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

        StackPane root = new StackPane(pirateBackground, content);

        buttonH.setOnAction(e -> {
            if (alarmMode) {
                alarmHour = (alarmHour + 1) % 24;
                updateDisplay();
            }
        });

        buttonM.setOnAction(e -> {
            if (alarmMode) {
                alarmMinute = (alarmMinute + 1) % 60;
                updateDisplay();
            }
        });

        buttonHMinus.setOnAction(e -> {
            if (alarmMode) {
                alarmHour = (alarmHour + 23) % 24;
                updateDisplay();
            }
        });

        buttonMMinus.setOnAction(e -> {
            if (alarmMode) {
                alarmMinute = (alarmMinute + 59) % 60;
                updateDisplay();
            }
        });

        buttonA.setOnAction(e -> {
            alarmMode = !alarmMode;
            updateDisplay();
        });

        Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (checkAlarm()) {
                pirateBackground.setVisible(true);
                sound.play();
                showAlert("Alarm!", "It's time for pirates, Yo-ho-ho!");
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

    static void updateDisplay() {
        labelTime.setText("Current: " + LocalTime.now().format(TIME_FORMATTER));

        if (alarmMode) {
            labelAlarm.setText("Alarm: " + String.format("%02d:%02d", alarmHour, alarmMinute));
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

    static boolean checkAlarm() {
        if (!alarmMode) return false;
        LocalTime now = LocalTime.now();
        return now.getHour() == alarmHour && now.getMinute() == alarmMinute && now.getSecond() == 0;
    }

    static void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setHeaderText(null);
        alert.setTitle(title);

        alert.setOnHidden(e -> {
            alarmMode = false;
            pirateBackground.setVisible(false);
            sound.stop();
            updateDisplay();
        });

        alert.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
