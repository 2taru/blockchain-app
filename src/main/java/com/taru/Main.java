package com.taru;

import com.taru.blockchain.Block;
import com.taru.blockchain.Blockchain;
import com.taru.constants.Constants;
import com.taru.cryptocurrency.Miner;
import com.taru.cryptocurrency.Transaction;
import com.taru.cryptocurrency.TransactionOutput;
import com.taru.cryptocurrency.Wallet;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Security;

public class Main {
    public static void main(String[] args) {

        Security.addProvider(new BouncyCastleProvider());

        //create wallets + blockchain + the single miner in the network
        Wallet userA = new Wallet();
        Wallet userB = new Wallet();
        Wallet lender = new Wallet();
        Blockchain chain = new Blockchain();
        Miner miner = new Miner();

        //create genesis transaction that sends 500 coins to userA:
        Transaction genesisTransaction = new Transaction(lender.getPublicKey(), userA.getPublicKey(), 500, null);
        genesisTransaction.generateSignature(lender.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.outputs.add(new TransactionOutput(genesisTransaction.getReceiver(), genesisTransaction.getAmount(), genesisTransaction.getTransactionId()));
        Blockchain.UTXOs.put(genesisTransaction.outputs.getFirst().getId(), genesisTransaction.outputs.getFirst());

        System.out.println("Constructing the genesis block...");
        Block genesis = new Block(Constants.GENESIS_PREV_HASH);
        genesis.addTransaction(genesisTransaction);
        miner.mine(genesis,chain);

        Block block1 = new Block(genesis.getHash());
        System.out.println("\nuserA's balance is: " + userA.calculateBalance());
        System.out.println("\nuserA tries to send money (120 coins) to userB...");
        block1.addTransaction(userA.transferMoney(userB.getPublicKey(), 120));
        miner.mine(block1,chain);
        System.out.println("\nuserA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());

        Block block2 = new Block(block1.getHash());
        System.out.println("\nuserA sends more funds (600) than it has...");
        block2.addTransaction(userA.transferMoney(userB.getPublicKey(), 600));
        miner.mine(block2,chain);
        System.out.println("\nuserA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());

        Block block3 = new Block(block2.getHash());
        System.out.println("\nuserB is attempting to send funds (110) to userA...");
        block3.addTransaction(userB.transferMoney( userA.getPublicKey(), 110));
        System.out.println("\nuserA's balance is: " + userA.calculateBalance());
        System.out.println("userB's balance is: " + userB.calculateBalance());
        miner.mine(block3,chain);

        System.out.println("Miner's reward: "+miner.getReward());
    }
}