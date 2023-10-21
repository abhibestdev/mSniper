package me.abhi.sniper.mojang.task;

import lombok.AllArgsConstructor;
import me.abhi.sniper.Sniper;
import me.abhi.sniper.mojang.MojangConnection;
import me.abhi.sniper.settings.SettingsHandler;
import me.abhi.sniper.util.MojangUtil;

import static me.abhi.sniper.util.ConsoleUtil.log;

@AllArgsConstructor
public class StatsCalculatorTask implements Runnable {

    private MojangConnection mojangConnection;

    @Override
    public void run() {
        SettingsHandler settingsHandler = Sniper.getSettingsHandler();

        long dropTime = settingsHandler.getDropTime();
        long changedTime = MojangUtil.getTimeChanged(settingsHandler.getUsername());

        log(" ");
        log("Name dropped: " + dropTime + " ms");
        log("Name changed: " + changedTime + " ms");
        log("Difference: " + (changedTime - dropTime) + " ms");
        log(" ");

        System.exit(0);
    }
}
