package com.taru.blockchain;

import com.taru.cryptocurrency.TransactionOutput;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Blockchain {

    /**
     * Public ledger. Everyone can access all the blocks (previous transactions) on the blockchain.
     */
    public static List<Block> blockChain;
    /**
     * Storage for every unspent transaction.
     */
    public static Map<String, TransactionOutput> UTXOs;

    public Blockchain() {
        Blockchain.UTXOs = new HashMap<String, TransactionOutput>();
        Blockchain.blockChain = new ArrayList<>();
    }

    public void addBlock(Block block) {
        Blockchain.blockChain.add(block);
    }

    public int size() {
        return Blockchain.blockChain.size();
    }

    @Override
    public String toString() {

        StringBuilder blockChain = new StringBuilder();

        for (Block block : Blockchain.blockChain) {
            blockChain.append(block.toString()).append("\n");
        }

        return blockChain.toString();
    }
}
