package com.taru.constants;

public class Constants {

    private Constants() {
    }

    // the number of leading zeros
    public static final int DIFFICULTY = 5;
    // SHA-256 value of the previous hash of the first block
    public static final String GENESIS_PREV_HASH = "0000000000000000000000000000000000000000000000000000000000000000";
    // amount of BTCs the miners get after the mining
    public static final double MINER_REWARD = 6.25;
}
