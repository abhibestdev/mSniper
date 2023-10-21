package me.abhi.sniper.account;

import lombok.Getter;
import lombok.SneakyThrows;
import me.abhi.sniper.Sniper;
import me.abhi.sniper.proxy.ProxyHandler;
import me.abhi.sniper.util.FileUtil;
import me.abhi.sniper.util.MojangUtil;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static me.abhi.sniper.util.ConsoleUtil.log;

public class AccountHandler {

    @Getter
    private List<Account> accounts;

    @SneakyThrows
    public AccountHandler() {
        //Initialize account list
        accounts = new ArrayList<>();

        loadAccounts();
    }

    //Load accounts
    private void loadAccounts() throws IOException {
        File file = new File("accounts.txt");

        //If accounts file doesn't exist, make a new one
        if (!file.exists()) {
            file.createNewFile();

            log("Created accounts.txt file.");
        }
        ProxyHandler proxyHandler = Sniper.getProxyHandler();

        AtomicInteger atomicIndex = new AtomicInteger();

        FileUtil.getLines(file).forEach(credentials -> {

            if (accounts.size() >= 10) return;

            String email = credentials.split(":")[0];
            String password = credentials.split(":")[1];

            Proxy proxy = null;

            if (atomicIndex.get() >= proxyHandler.getProxies().size()) {
                atomicIndex.set(0);

                if (proxyHandler.getProxies().size() > 0) {

                    String rawProxy = proxyHandler.getProxies().get(atomicIndex.getAndAdd(1));
                    String ip = rawProxy.split(":")[0];
                    int port = Integer.parseInt(rawProxy.split(":")[1]);

                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
                }
            }


            try {
                String[] data = MojangUtil.data(email, password, proxy);

                String authToken = data[0];
                String uuid = data[1];

                //Add account details to the list of accounts
                Account account = new Account(email, password, authToken, uuid);
                accounts.add(account);
            } catch (Exception ex) {
                ex.printStackTrace();
                log("Error loading account " + email + ":" + password);
            }

        });
        log("Successfully loaded " + accounts.size() + " account" + (accounts.size() == 1 ? "" : "s") + "!");
    }

    public void removeAccountFromFile(Account account) {
        File file = new File("accounts.txt");
        FileUtil.removeLineFromFile(file.getAbsolutePath(), account.getEmail() + ":" + account.getPassword());
    }
}