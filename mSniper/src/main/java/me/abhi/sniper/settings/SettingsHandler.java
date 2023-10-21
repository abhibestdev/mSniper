package me.abhi.sniper.settings;

import lombok.Getter;
import me.abhi.sniper.Sniper;
import me.abhi.sniper.mutliplier.MultiplierHandler;
import me.abhi.sniper.util.MojangUtil;
import me.abhi.sniper.util.TimeUtil;

import java.util.Scanner;

public class SettingsHandler {

    @Getter private String username;
    @Getter private long dropTime;
    @Getter private int delayMultiplier;
    @Getter private long optimalDelay;

    public SettingsHandler() {
        //Request username from the user
        requestInformation();
    }

    private void requestInformation() {
        Scanner scanner = new Scanner(System.in);

        //Get the desired username
        System.out.println("Please enter the desired username");
        username = scanner.next();

        MultiplierHandler multiplierHandler = Sniper.getMultiplierHandler();
        //Get the target difference
 /*       System.out.println("Please enter the target difference");
        int targetDifference = scanner.nextInt();

        delayMultiplier = multiplierHandler.getOptimalMultiplier(targetDifference);

        //If the delay multiplier is 0, we know there wasn't any data to be found, so we ask the user to enter their own
        if (delayMultiplier == 0L) {
            //Get the delay multiplier
            System.out.println("Please enter the delay multiplier");
            delayMultiplier = scanner.nextInt();
        } */
  //      delayMultiplier = multiplierHandler.getOptimalMultiplier();
    //    System.out.println("Optimal Multiplier: " + delayMultiplier);

//        optimalDelay = multiplierHandler.getAverageDelay();
   //     System.out.println("Optimal Delay: " + multiplierHandler.getAverageDelay());

//        double optimalMultiplier = optimalDelay / 5;
  //      System.out.println("Mathematically Optimal Multiplier: " + optimalMultiplier);

    //    delayMultiplier = (int) optimalMultiplier;

        //Get the drop time of the new username
        dropTime = MojangUtil.getDropTime(username);
        if (dropTime == 0L) {
            System.out.println("That username is not dropping.");

            //Exit the program
            System.exit(0);
        }
        System.out.println("Drop Time: " + TimeUtil.formatTimeMillis(dropTime - System.currentTimeMillis(), false, true));
    }
}
