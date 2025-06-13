package org.example;

import java.sql.*;
import java.time.LocalDateTime;

public class DBLogger {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/alarm_monitoring";
    private static final String USER = "alarm_user";
    private static final String PASS = "alarm_pass";

    public static void log(String eventType, String description) {
        String sql = "INSERT INTO events (timestamp, event_type, description) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(2, eventType);
            pstmt.setString(3, description);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

