package com.corebanker.enums;

public enum TransactionStatus {
    PENDING,   // Transaction en attente
    PROCESSING, // Transaction en cours
    SUCCEEDED,  // Transaction réussie
    FAILED,     // Transaction échouée
    CANCELLED;   // Transaction annulée

    @Override
    public String toString() {
        return switch (this) {
            case PENDING -> "PENDING";
            case PROCESSING -> "PROCESSING";
            case SUCCEEDED -> "SUCCEEDED";
            case FAILED -> "FAILED";
            case CANCELLED -> "CANCELLED";
        };
    }
}
