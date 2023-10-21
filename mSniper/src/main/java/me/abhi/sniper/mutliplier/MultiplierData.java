package me.abhi.sniper.mutliplier;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class MultiplierData {

    @Getter private int multiplier;
    @Getter private long delay;
    @Getter private long difference;
}
