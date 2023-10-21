package me.abhi.sniper.success;

import lombok.SneakyThrows;
import me.abhi.sniper.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import static me.abhi.sniper.util.ConsoleUtil.log;

public class SuccessHandler {

    private List<String> pastSnipes;

    public SuccessHandler() {
        load();
    }

    @SneakyThrows
    private void load() {
        File file = new File("snipes.txt");

        //If file doesn't exist, create one
        if (!file.exists()) {
            file.createNewFile();

            log("Created snipes.txt file.");
        }
        //Save past snipes
        pastSnipes = FileUtil.getLines(file);
    }

    @SneakyThrows
    public void save(String snipe) {
        File file = new File("snipes.txt");

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        pastSnipes.add(snipe);

        for (int i = 0; i < pastSnipes.size(); i++) {
            String line = pastSnipes.get(i);
            if (i > 0) {
                bufferedWriter.write("\n");
            }
            bufferedWriter.write(line);

        }
        bufferedWriter.close();
    }
}
