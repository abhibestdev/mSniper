package me.abhi.sniper.proxy;

import lombok.Getter;
import lombok.SneakyThrows;
import me.abhi.sniper.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static me.abhi.sniper.util.ConsoleUtil.log;

public class ProxyHandler {

    @Getter private List<String> proxies;

    @SneakyThrows
    public ProxyHandler() {
        //Initialize proxy list
        proxies = new ArrayList<>();

        loadProxies();
    }

    private void loadProxies() throws IOException {
        File file = new File("proxies.txt");

        //If accounts file doesn't exist, make a new one
        if (!file.exists()) {
            file.createNewFile();

            log("Created proxies.txt file.");
        }
        FileUtil.getLines(file).forEach(proxy -> {
            proxies.add(proxy);
        });
    }
}
