package com.corebanker;

import com.corebanker.models.BankAccount;
import com.corebanker.models.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================= Bienvenue dans l'application Core Banking =======================================");

        // Création de deux comptes bancaires avec des soldes initiaux
        BankAccount account1 = new BankAccount("Alice", 1000);
        BankAccount account2 = new BankAccount("Bob", 500);

        // Affichage des soldes initiaux des comptes
        System.out.println("\n=== Soldes initiaux ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        // Effectuer une transaction de 200€ d'Alice vers Bob
        Transaction transaction1 = new Transaction(account1, account2, 200);
        transaction1.processTransaction();

        // Effectuer une transaction de 50€ de Bob vers Alice
        Transaction transaction2 = new Transaction(account2, account1, 50);
        transaction2.processTransaction();

        // Afficher l'historique des transactions de chaque compte
        account1.displayTransactionHistory();
        account2.displayTransactionHistory();

    }
}