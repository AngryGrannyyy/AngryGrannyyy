public class AlarmClock implements Runnable {
    private final ClockLogic logic;

    public AlarmClock(ClockLogic logic) {
        this.logic = logic;
    }

    @Override
    public void run() {
        while (true) {
            if (logic.checkAlarm()) {
                System.out.println("\n⏰ Будильник сработал!");
            }
            try {
                Thread.sleep(1000); // Проверка каждую секунду
            } catch (InterruptedException e) {
                System.out.println("Поток прерван.");
                return;
            }
        }
    }
}
