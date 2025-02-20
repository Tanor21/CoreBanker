package com.corebanker;

import com.corebanker.models.BankAccount;
import com.corebanker.models.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================= Bienvenue dans l'application Core Banking =======================================");

        // Création de deux comptes bancaires
        BankAccount account1 = new BankAccount("Alice", 1000);
        BankAccount account2 = new BankAccount("Bob", 500);

        // Affichage des soldes initiaux
        System.out.println("\n=== Soldes initiaux ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        // Création et exécution d'une transaction
        Transaction transaction = new Transaction(account1, account2, 200);
        transaction.processTransaction();

        // Affichage des soldes après transaction
        System.out.println("\n=== Soldes après transaction ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        // Affichage des détails de la transaction
        transaction.displayTransactionDetails();

    }
}