package com.taru.cryptocurrency;

import com.taru.blockchain.Block;
import com.taru.blockchain.Blockchain;
import com.taru.constants.Constants;

public class Miner {

    private double reward;

    /**
     * Method that places a mined block to the blockchain
     *
     * @param block      block to be mined
     * @param blockChain blockchain to put the mined block
     */
    public void mine(Block block, Blockchain blockChain) {
        while (!isGoldenHash(block)) {
            block.incrementNonce();
            block.generateHash();
        }

        System.out.println(block + " has just mined...");
        System.out.println("Hash is: " + block.getHash());

        blockChain.addBlock(block);
        reward += Constants.MINER_REWARD;
    }

    /**
     * Method that checks if the miner has found a golden hash
     *
     * @param block block to check
     * @return true if a golden hash is found, false otherwise
     */
    public boolean isGoldenHash(Block block) {
        String leadingZeros = new String(new char[Constants.DIFFICULTY]).replace('\0', '0');
        return block.getHash().substring(0, Constants.DIFFICULTY).equals(leadingZeros);
    }

    public double getReward() {
        return this.reward;
    }
}
