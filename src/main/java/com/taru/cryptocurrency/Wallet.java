package com.taru.cryptocurrency;

import com.taru.blockchain.Blockchain;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wallet {

    /**
     * The private key used to sign transactions.
     */
    private final PrivateKey privateKey;
    /**
     * The public key used to verify the transaction.
     */
    private final PublicKey publicKey;

    public Wallet() {
        KeyPair keyPair = CryptographyHelper.generateKeys();
        this.privateKey = keyPair.getPrivate();
        this.publicKey = keyPair.getPublic();
    }

    /**
     * Transfer given amount of coins from the sender to the receiver.
     *
     * @param receiver User that receive coins.
     * @param amount   Amount of coins sent.
     * @return Transaction that miners of the blockchain will put into the blockchain.
     */
    public Transaction transferMoney(PublicKey receiver, double amount) {

        if (calculateBalance() < amount) {
            System.out.println("Invalid transaction because of not enough money...");
            return null;
        }

        List<TransactionInput> inputs = new ArrayList<>();

        for (Map.Entry<String, TransactionOutput> item : Blockchain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();

            if (UTXO.isMine(this.publicKey))
                inputs.add(new TransactionInput(UTXO.getId()));
        }

        Transaction newTransaction = new Transaction(publicKey, receiver, amount, inputs);
        newTransaction.generateSignature(privateKey);

        return newTransaction;
    }

    /**
     * Calculate current balance based on UTXO's associated with current user.
     *
     * @return User balance.
     */
    public double calculateBalance() {

        double balance = 0;

        for (Map.Entry<String, TransactionOutput> item : Blockchain.UTXOs.entrySet()) {

            TransactionOutput transactionOutput = item.getValue();

            if (transactionOutput.isMine(publicKey)) {
                balance += transactionOutput.getAmount();
            }
        }

        return balance;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
