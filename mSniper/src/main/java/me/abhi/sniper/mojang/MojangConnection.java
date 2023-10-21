package me.abhi.sniper.mojang;

import lombok.Getter;
import lombok.SneakyThrows;
import me.abhi.sniper.account.Account;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;

public class MojangConnection {

    @Getter private Account account;
    private String newUsername;

    private HttpURLConnection httpURLConnection;

    @SneakyThrows
    public MojangConnection(Account account, String newUsername, Proxy proxy) {
        this.account = account;
        this.newUsername = newUsername;

        //Send request to security endpoint before we access the profile
        sendSecurityRequest(account, proxy);

        //Open connection
        URL url = new URL("https://api.minecraftservices.com/user/profile/" + account.getUuid() + "/name");
        httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + account.getBearerToken());

    }

    @SneakyThrows
    private void sendSecurityRequest(Account account, Proxy proxy) {
        URL url = new URL("https://api.mojang.com/user/security/challenges");
        HttpURLConnection httpURLConnection = (HttpURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setRequestProperty("Authorization", "Bearer " + account.getBearerToken());

        StringBuilder content;
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(httpURLConnection.getInputStream()))) {

            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) {

                content.append(line);
                content.append(System.lineSeparator());
            }
        }
    }

    @SneakyThrows
    public void requestNameChange() {
        String jsonInputString = "{\"name\":\"" + newUsername + "\", \"password\":\"" + account.getPassword() + "\"}";
        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
    }

    @SneakyThrows
    public String getResponse() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        }
    }

    public void disconnect() {
        httpURLConnection.disconnect();
    }
}
