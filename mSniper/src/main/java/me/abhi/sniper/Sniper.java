package me.abhi.sniper;

import lombok.Getter;
import lombok.Setter;
import me.abhi.sniper.account.AccountHandler;
import me.abhi.sniper.mojang.MojangHandler;
import me.abhi.sniper.mutliplier.MultiplierHandler;
import me.abhi.sniper.proxy.ProxyHandler;
import me.abhi.sniper.settings.SettingsHandler;
import me.abhi.sniper.success.SuccessHandler;
import me.abhi.sniper.util.ConsoleUtil;

public class Sniper {

    @Setter @Getter private static boolean keepTrying;

    @Getter private static MultiplierHandler multiplierHandler;
    @Getter private static SuccessHandler successHandler;
    @Getter private static SettingsHandler settingsHandler;
    @Getter private static ProxyHandler proxyHandler;
    @Getter private static AccountHandler accountHandler;
    @Getter private static MojangHandler mojangHandler;

    public static void main(String[] args) {
        //Clear console when the program starts
        ConsoleUtil.clearConsole();

        //Register handlers
        registerHandlers();
    }

    private static void registerHandlers() {
        multiplierHandler = new MultiplierHandler();
        successHandler = new SuccessHandler();
        settingsHandler = new SettingsHandler();
   //     proxyHandler = new ProxyHandler();
     //   accountHandler = new AccountHandler();
       // mojangHandler = new MojangHandler();
    }
}
