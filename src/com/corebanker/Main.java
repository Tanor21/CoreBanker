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

        // Tentative de transaction avec un montant invalide
        Transaction invalidTransaction = new Transaction(account1, account2, -200);
        invalidTransaction.processTransaction(); // Cela doit échouer avec un message d'erreur

        // Tentative de transaction avec un solde insuffisant
        Transaction insufficientFundsTransaction = new Transaction(account2, account1, 600);
        insufficientFundsTransaction.processTransaction(); // Cela doit échouer avec un message d'erreur

        // Transaction valide
        Transaction validTransaction = new Transaction(account1, account2, 200);
        validTransaction.processTransaction(); // Cela doit réussir

        // Affichage des soldes après transaction
        System.out.println("\n=== Soldes après transaction ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        // Affichage des détails de la transaction
        validTransaction.displayTransactionDetails();

    }
}