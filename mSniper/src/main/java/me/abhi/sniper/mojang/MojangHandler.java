package me.abhi.sniper.mojang;

import lombok.Getter;
import lombok.SneakyThrows;
import me.abhi.sniper.Sniper;
import me.abhi.sniper.account.Account;
import me.abhi.sniper.account.AccountHandler;
import me.abhi.sniper.mojang.task.MojangRequestTask;
import me.abhi.sniper.settings.SettingsHandler;
import me.abhi.sniper.util.StringUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MojangHandler {

    @Getter private List<MojangConnection> mojangConnectionList;
    @Getter private ScheduledExecutorService scheduledExecutorService;

    public MojangHandler() {
        //Initialize list
        mojangConnectionList = new ArrayList<>();

        load();
    }

    public void load() {
        SettingsHandler settingsHandler = Sniper.getSettingsHandler();
        AccountHandler accountHandler = Sniper.getAccountHandler();

        //Set keep trying to true
        Sniper.setKeepTrying(true);

        //Make 10 Mojang tasks per thread per account
        scheduledExecutorService = Executors.newScheduledThreadPool(15);
        accountHandler.getAccounts().forEach(account -> {
            for (int i = 0; i < 5; i++) {
                scheduledExecutorService.scheduleAtFixedRate(new MojangRequestTask(new MojangConnection(account, settingsHandler.getUsername(), null), (i * settingsHandler.getDelayMultiplier()), settingsHandler.getDelayMultiplier()), 0L, 500L, TimeUnit.NANOSECONDS);
            }
        });
    }

    @SneakyThrows
    public void changeSkin(Account account, Proxy proxy) {
        URL url = new URL("https://api.mojang.com/user/profile/" + account.getUuid() + "/skin");
        HttpURLConnection httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + account.getBearerToken());

        String inputString = "model=slim&url=" + StringUtil.encodeValue("https://cdn.discordapp.com/attachments/761691011084189726/765005534507040839/0276642e49b4d1b250d2e3ec7e5109681d9d6e97.png");

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = inputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
        }
    }
}
