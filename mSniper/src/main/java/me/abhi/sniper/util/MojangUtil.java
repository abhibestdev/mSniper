package me.abhi.sniper.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Proxy;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MojangUtil {

    public static String[] data(String username, String password, Proxy proxy) throws Exception {

        String genClientToken = UUID.randomUUID().toString();

        // Setting up json POST request
        String payload = "{\"agent\": {\"name\": \"Minecraft\",\"version\": 1},\"username\": \"" + username
                + "\",\"password\": \"" + password + "\",\"clientToken\": \"" + genClientToken + "\"}";

        String output = postReadURL(payload, new URL("https://authserver.mojang.com/authenticate"), proxy);

        JSONObject jsonObject = (JSONObject) new JSONParser().parse(output);
        JSONObject selectedProfile = (JSONObject) jsonObject.get("selectedProfile");
        String id = (String) selectedProfile.get("id");

        // Setting up patterns
        String authBeg = "{\"accessToken\":\"";
        String authEnd = "\",\"clientToken\":\"";
        String clientEnd = "\",\"selectedProfile\"";

        // What we are looking for
        String authtoken = getStringBetween(output, authBeg, authEnd);
        return new String[]{authtoken, id};
    }

    private static String postReadURL(String payload, URL url, Proxy proxy) throws Exception {
        HttpsURLConnection con = (HttpsURLConnection) (proxy != null ? url.openConnection(proxy) : url.openConnection());
        con.setReadTimeout(15000);
        con.setConnectTimeout(15000);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setDoInput(true);
        con.setDoOutput(true);

        OutputStream out = con.getOutputStream();
        out.write(payload.getBytes("UTF-8"));
        out.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

        String output = "";
        String line = null;
        while ((line = in.readLine()) != null)
            output += line;

        in.close();

        return output;
    }

    private static String getStringBetween(String base, String begin, String end) {

        Pattern patbeg = Pattern.compile(Pattern.quote(begin));
        Pattern patend = Pattern.compile(Pattern.quote(end));

        int resbeg = 0;
        int resend = base.length() - 1;

        Matcher matbeg = patbeg.matcher(base);

        if (matbeg.find())
            resbeg = matbeg.end();

        Matcher matend = patend.matcher(base);

        if (matend.find())
            resend = matend.start();

        return base.substring(resbeg, resend);
    }

    public static long getDropTime(String username) {
        long dropTime = 0L;
        try {
            JSONParser jsonParser = new JSONParser();

            System.out.println(JsonReader.getLine(new URL("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + ((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(37))))));

            JSONObject idObject = (JSONObject) jsonParser.parse(JsonReader.getLine(new URL("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + ((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(37)) / 1000))));
            String id = (String) idObject.get("id");

            JSONArray jsonArray = (JSONArray) new JSONParser().parse(JsonReader.getLine(new URL("https://api.mojang.com/user/profiles/" + id + "/names")));

            int currentName = 0;

            JSONObject lastNameObject = null;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;

                String name = (String) jsonObject.get("name");
                if (name.equalsIgnoreCase(username)) {
                    lastNameObject = jsonObject;
                }
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i).equals(lastNameObject)) {
                    currentName = i;
                    break;
                }
            }

            JSONObject nextNameObject = (JSONObject) jsonArray.get(currentName + 1);
            long changedToAt = (long) nextNameObject.get("changedToAt");

            dropTime = changedToAt + TimeUnit.DAYS.toMillis(37);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dropTime;
    }

    public static long getTimeChanged(String username) {
        try {
            JSONParser jsonParser = new JSONParser();

            JSONObject idObject = (JSONObject) jsonParser.parse(JsonReader.getLine(new URL("https://api.mojang.com/users/profiles/minecraft/" + username + "?at=" + ((System.currentTimeMillis() - TimeUnit.DAYS.toMillis(37)) / 1000))));
            String id = (String) idObject.get("id");

            JSONArray jsonArray = (JSONArray) new JSONParser().parse(JsonReader.getLine(new URL("https://api.mojang.com/user/profiles/" + id + "/names")));

            int currentName = 0;

            JSONObject lastNameObject = null;
            for (Object o : jsonArray) {
                JSONObject jsonObject = (JSONObject) o;

                String name = (String) jsonObject.get("name");
                if (name.equalsIgnoreCase(username)) {
                    lastNameObject = jsonObject;
                }
            }

            for (int i = 0; i < jsonArray.size(); i++) {
                if (jsonArray.get(i).equals(lastNameObject)) {
                    currentName = i;
                    break;
                }
            }

            JSONObject nextNameObject = (JSONObject) jsonArray.get(currentName);
            long changedToAt = (long) nextNameObject.get("changedToAt");

            return changedToAt;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0L;
    }
}
