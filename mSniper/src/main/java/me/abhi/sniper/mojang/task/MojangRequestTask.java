package me.abhi.sniper.mojang.task;

import lombok.AllArgsConstructor;
import me.abhi.sniper.Sniper;
import me.abhi.sniper.account.Account;
import me.abhi.sniper.account.AccountHandler;
import me.abhi.sniper.mojang.MojangConnection;
import me.abhi.sniper.mojang.MojangHandler;
import me.abhi.sniper.mutliplier.MultiplierHandler;
import me.abhi.sniper.settings.SettingsHandler;
import me.abhi.sniper.success.SuccessHandler;

import static me.abhi.sniper.util.ConsoleUtil.log;

@AllArgsConstructor
public class MojangRequestTask implements Runnable {

    private MojangConnection mojangConnection;
    private long delay;
    private long multiplier;

    @Override
    public void run() {
        SettingsHandler settingsHandler = Sniper.getSettingsHandler();
        MojangHandler mojangHandler = Sniper.getMojangHandler();
        MultiplierHandler multiplierHandler = Sniper.getMultiplierHandler();
        AccountHandler accountHandler = Sniper.getAccountHandler();
        SuccessHandler successHandler = Sniper.getSuccessHandler();

        if (settingsHandler.getDropTime() - System.currentTimeMillis() <= settingsHandler.getOptimalDelay() && Sniper.isKeepTrying()) {

            long requestTime = System.currentTimeMillis();
            log("Requesting name change...");
            mojangConnection.requestNameChange();

            if (mojangConnection.getResponse().isEmpty()) {

                long time = System.currentTimeMillis();

                mojangHandler.changeSkin(mojangConnection.getAccount(), null);
                log(" ");
                log("Snipe successful. New username: \"" + settingsHandler.getUsername() + "\"!");
                log("Requested: " + requestTime);
                log("Response: " + time);
                log("Difference: " + (time - requestTime) + " ms");
                log("Tested delay: " + delay + " ms");

                Account account = mojangConnection.getAccount();

                log("Credentials: " + account.getEmail() + ":" + account.getPassword());
                log(" ");

      //          multiplierHandler.save((int) multiplier, delay, (time - requestTime));
                accountHandler.removeAccountFromFile(account);
                successHandler.save(settingsHandler.getUsername() + ": " + account.getEmail() + ":" + account.getPassword());

                Sniper.setKeepTrying(false);


                System.exit(0);
            }
        }
    }
}
