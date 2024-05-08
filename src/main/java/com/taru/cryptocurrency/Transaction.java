package com.taru.cryptocurrency;

import com.taru.blockchain.Blockchain;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class Transaction {

    /**
     * Transaction id as SHA-256 hash.
     */
    private String transactionId;
    /**
     * Sender's public key.
     */
    private PublicKey sender;
    /**
     * Receiver's public key.
     */
    private PublicKey receiver;
    /**
     * The amount of coins that the transaction sends to the receiver from the sender.
     */
    private double amount;
    /**
     * A transaction signature signed by the sender to prevent other users from spending coins other than theirs.
     */
    private byte[] signature;
    /**
     * Current transaction inputs.
     */
    public List<TransactionInput> inputs;
    /**
     * Current transaction outputs.
     */
    public List<TransactionOutput> outputs;

    public Transaction(PublicKey sender, PublicKey receiver, double amount, List<TransactionInput> inputs) {
        this.transactionId = calculateHash();
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
        this.inputs = inputs;
        this.outputs = new ArrayList<>();
    }

    /**
     * Verify transaction and perform UTXO update.
     * @return true if transaction is valid, false otherwise.
     */
    public boolean verifyTransaction() {

        if (!verifySignature()) {
            System.out.println("Invalid transaction because of invalid signature...");
            return false;
        }

        for (TransactionInput transactionInput : inputs) {
            transactionInput.setUTXO(Blockchain.UTXOs.get(transactionInput.getTransactionOutputId()));
        }

        if (getInputsSum() - amount < 0) {
            System.out.println("Invalid transaction because of insufficient funds...");
            return false;
        }
        outputs.add(new TransactionOutput(this.receiver, amount, transactionId));
        if (getInputsSum() - amount > 0) {
            outputs.add(new TransactionOutput(this.sender, getInputsSum() - amount, transactionId));
        }

        for (TransactionOutput transactionOutput : outputs) {
            Blockchain.UTXOs.put(transactionOutput.getId(), transactionOutput);
        }

        for (TransactionInput transactionInput : inputs) {
            if (transactionInput.getUTXO() != null) {
                Blockchain.UTXOs.remove(transactionInput.getUTXO().getId());
            }
        }

        return true;
    }

    /**
     * Counts how many coins (money) the sender has.
     *
     * @return The amount of coins (money) of the sender.
     */
    private double getInputsSum() {

        double sum = 0;

        for (TransactionInput transactionInput : inputs) {
            if (transactionInput.getUTXO() != null) {
                sum += transactionInput.getUTXO().getAmount();
            }
        }

        return sum;
    }

    /**
     * Sign data based on private key.
     *
     * @param privateKey Sender private key.
     */
    public void generateSignature(PrivateKey privateKey) {

        String data = sender.toString() + receiver.toString() + amount;
        signature = CryptographyHelper.sign(privateKey, data);
    }

    /**
     * Verifies that the transaction was signed by the sender using the sender's public key.
     *
     * @return true if the sender signed the current transaction, false otherwise.
     */
    public boolean verifySignature() {

        String data = sender.toString() + receiver.toString() + amount;
        return CryptographyHelper.verify(sender, data, signature);
    }

    /**
     * Generate transaction id using SHA-256.
     *
     * @return 64 hexadecimal characters as {@link java.lang.String}
     */
    private String calculateHash() {

        String hashData = sender.toString() + receiver.toString() + amount;
        return CryptographyHelper.generateHash(hashData);
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public PublicKey getSender() {
        return sender;
    }

    public void setSender(PublicKey sender) {
        this.sender = sender;
    }

    public PublicKey getReceiver() {
        return receiver;
    }

    public void setReceiver(PublicKey receiver) {
        this.receiver = receiver;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
    }
}
