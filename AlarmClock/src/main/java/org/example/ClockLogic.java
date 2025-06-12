package org.example;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockLogic {
    private int alarmHour = 0;
    private int alarmMinute = 0;
    private boolean alarmMode = false;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public String getCurrentTime() {
        return LocalTime.now().format(TIME_FORMATTER);
    }

    public boolean checkAlarm() {
        if (!alarmMode) return false;

        LocalTime now = LocalTime.now();
        boolean isTime = now.getHour() == alarmHour && now.getMinute() == alarmMinute && now.getSecond() == 0;

        return isTime;
    }

    public void toggleAlarmMode() {
        alarmMode = !alarmMode;
    }

    public void increaseAlarmHour() {
        if (alarmMode) {
            alarmHour = (alarmHour + 1) % 24;
        }
    }

    public void increaseAlarmMinute() {
        if (alarmMode) {
            alarmMinute = (alarmMinute + 1) % 60;
        }
    }

    public void decreaseAlarmHour() {
        if (alarmMode) {
            alarmHour = (alarmHour + 23) % 24;
        }
    }

    public void decreaseAlarmMinute() {
        if (alarmMode) {
            alarmMinute = (alarmMinute + 59) % 60;
        }
    }

    public String getAlarmTime() {
        return String.format("%02d:%02d", alarmHour, alarmMinute);
    }

    public boolean isAlarmOn() {
        return alarmMode;
    }

    public void setAlarmMode(boolean mode) {
        alarmMode = mode;
    }
}
