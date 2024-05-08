package com.taru.blockchain;

import com.taru.constants.Constants;
import com.taru.cryptocurrency.CryptographyHelper;
import com.taru.cryptocurrency.Miner;
import com.taru.cryptocurrency.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    /**
     * Hash of this block.
     */
    private String hash;
    /**
     * Hash of the previous block to which this block is attached.
     */
    private final String previousHash;
    /**
     * Block creation time.
     */
    private final long timeStamp;
    /**
     * All transactions within this block.
     */
    public List<Transaction> transactions;
    /**
     * This value is used by miners ({@link Miner}) to find golden hash.
     */
    private int nonce;

    public Block(String previousHash) {
        this.transactions = new ArrayList<>();
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        generateHash();
    }

    /**
     * Generate hash for current block.}
     */
    public void generateHash() {
        String dataToHash = previousHash + timeStamp +
                transactions.toString() + nonce;
        this.hash = CryptographyHelper.generateHash(dataToHash);
    }

    /**
     * Add valid transactions to the block.
     *
     * @param transaction transaction to add.
     * @return true if transaction vas added, false otherwise.
     */
    public boolean addTransaction(Transaction transaction) {

        if (transaction == null) return false;

        if ((!previousHash.equals(Constants.GENESIS_PREV_HASH))) {
            if ((!transaction.verifyTransaction())) {
                System.out.println("Transaction is not valid...");
                return false;
            }
        }

        transactions.add(transaction);
        System.out.println("Transaction is valid and it's added to the block " + this);
        return true;
    }

    /**
     * This method is used by miners ({@link Miner}) to find golden hash.
     */
    public void incrementNonce() {
        this.nonce++;
    }

    public String getHash() {
        return this.hash;
    }

    @Override
    public String toString() {
        return "Block{" +
                "hash='" + hash + '\'' +
                ", previousHash='" + previousHash + '\'' +
                ", timeStamp=" + timeStamp +
                ", transactions=" + transactions +
                ", nonce=" + nonce +
                '}';
    }
}
