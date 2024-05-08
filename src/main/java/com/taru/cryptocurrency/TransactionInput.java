package com.taru.cryptocurrency;

public class TransactionInput {

    /**
     * This id is the transactionId of the TransactionOutput.
     */
    private String transactionOutputId;
    /**
     * Unspent transaction output.
     */
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public void setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput uTXO) {
        this.UTXO = uTXO;
    }
}
