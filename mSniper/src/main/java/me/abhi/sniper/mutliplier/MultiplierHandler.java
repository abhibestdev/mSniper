package me.abhi.sniper.mutliplier;

import lombok.SneakyThrows;
import me.abhi.sniper.util.FileUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static me.abhi.sniper.util.ConsoleUtil.log;

public class MultiplierHandler {

    private List<MultiplierData> multiplierData;

    public MultiplierHandler() {
        //Initialize list
        multiplierData = new ArrayList<>();

        load();
    }

    @SneakyThrows
    private void load() {
        File file = new File("multiplier.txt");

        //If file doesn't exist, create one
        if (!file.exists()) {
            file.createNewFile();

            log("Created multiplier.txt file.");
        }

        FileUtil.getLines(file).forEach(line -> {
            int mutliplier = Integer.parseInt(line.split(":")[0]);
            long delay = Long.parseLong(line.split(":")[1]);
            long difference = Long.parseLong(line.split(":")[2]);

            //Add data to list
            multiplierData.add(new MultiplierData(mutliplier, delay, difference));
        });
    }

    public int getOptimalMultiplier(int targetDifference) {

        //Find all the multiplier data we have for the target difference
        List<MultiplierData> targetData = multiplierData.stream().filter(m -> m.getDifference() <= targetDifference).collect(Collectors.toList());

        if (targetData.size() < 5) {
            log("We don't have enough data to find a good multiplier.");
            return 0;
        }
        AtomicLong atomicMultiplier = new AtomicLong();
        targetData.forEach(m -> atomicMultiplier.addAndGet(m.getMultiplier()));
        atomicMultiplier.set(Math.round(atomicMultiplier.get() / targetData.size()));

        log("Found " + targetData.size() + " snipes that have reached the target difference.");
        log("Using that data, we found " + atomicMultiplier.get() + " would be the optimal multiplier.");
        return (int) atomicMultiplier.get();
    }

    public int getOptimalMultiplier() {
        List<Integer> multipliers = new ArrayList<>();

        long lowestDifference = 1000L;
        for (MultiplierData m : multiplierData) {
            if (lowestDifference > m.getDifference()) {
                lowestDifference = m.getDifference();
            }
        }
        for (MultiplierData m : multiplierData) {
            if (Math.abs(m.getDifference() - lowestDifference) <= 80) {
                multipliers.add(m.getMultiplier());
            }
        }
        int multiplier = 0;
        for (int m : multipliers) {
            multiplier += m;
        }
        return (multiplier / multipliers.size());
    }

    public long getAverageDelay() {
        long delay = 0L;
        for (MultiplierData m : multiplierData) {
            delay += m.getDelay();
        }
        return (delay / multiplierData.size());
    }

    public long getOptimalDelay() {
        List<Long> delays = new ArrayList<>();

        long lowestDifference = 1000L;
        for (MultiplierData m : multiplierData) {
            if (lowestDifference > m.getDifference()) {
                lowestDifference = m.getDifference();
            }
        }
        for (MultiplierData m : multiplierData) {
            if (Math.abs(m.getDifference() - lowestDifference) <= 80) {
                delays.add(m.getDelay());
            }
        }
        long delay = 0;
        for (long d : delays) {
            delay += d;
        }
        return (delay / delays.size());
    }

    @SneakyThrows
    public void save(int multiplier, long delay, long difference) {
        File file = new File("multiplier.txt");

        multiplierData.add(new MultiplierData(multiplier, delay, difference));

        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

        for (int i = 0; i < multiplierData.size(); i++) {
            MultiplierData m = multiplierData.get(i);
            if (i > 0) {
                bufferedWriter.write("\n");
            }
            bufferedWriter.write(m.getMultiplier() + ":" + m.getDelay() + ":" + m.getDifference());

        }
        bufferedWriter.close();
    }
}
