package me.abhi.sniper.util;


import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConsoleUtil {

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void log(String message) {
        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        System.out.println("[" + localTime.format(formatter) + "] " + message);
    }
}
