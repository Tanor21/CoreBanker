package com.corebanker.enums;

public enum TransactionType {
    TRANSFER, DEPOSIT, WITHDRAWAL;

    @Override
    public String toString() {
        return switch (this) {
            case TRANSFER -> "TRANSFER";
            case DEPOSIT -> "DEPOSIT";
            case WITHDRAWAL -> "WITHDRAWAL";
        };
    }
}
