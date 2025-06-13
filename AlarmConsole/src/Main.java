import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ClockLogic logic = new ClockLogic();
        Thread alarmThread = new Thread(new AlarmClock(logic));
        alarmThread.setDaemon(true);
        alarmThread.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("📟 Консольный будильник. Команды:");
        System.out.println("  A - Включить/выключить будильник");
        System.out.println("  H - Увеличить час");
        System.out.println("  M - Увеличить минуту");
        System.out.println("  1 - Показать текущее время и будильник");
        System.out.println("  0 - Выход");

        while (true) {
            System.out.print("\nВведите команду: ");
            String input = scanner.nextLine();
            switch (input) {
                case "a":
                case "A":
                    logic.toggleAlarmMode();
                    System.out.println("Будильник " + (logic.isAlarmOn() ? "включен" : "выключен"));
                    break;
                case "h":
                case "H":
                    logic.increaseAlarmHour();
                    System.out.println("Час будильника: " + logic.getAlarmTime());
                    break;
                case "m":
                case "M":
                    logic.increaseAlarmMinute();
                    System.out.println("Минута будильника: " + logic.getAlarmTime());
                    break;
                case "1":
                    System.out.println("Текущее время: " + logic.getCurrentTime());
                    System.out.println("Будильник: " + (logic.isAlarmOn() ? "вкл, на " + logic.getAlarmTime() : "выкл"));
                    break;
                case "0":
                    System.out.println("Выход...");
                    return;
                default:
                    System.out.println("Неверная команда.");
            }
        }
    }
}
