package com.corebanker;

import com.corebanker.managers.BankAccountManager;
import com.corebanker.models.BankAccount;
import com.corebanker.models.Transaction;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================= Bienvenue dans l'application Core Banking =======================================");

        // Création de deux comptes bancaires avec des soldes initiaux
        BankAccount account1 = new BankAccount("Alice", 1000);
        BankAccount account2 = new BankAccount("Bob", 500);

        // Ajout des comptes au gestionnaire
        BankAccountManager.addAccount(account1);
        BankAccountManager.addAccount(account2);

        // Affichage des soldes initiaux des comptes
        System.out.println("\n=== Soldes initiaux ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        // Effectuer des transactions
        System.out.println("\n=== Exécution des transactions ===");

        // Transaction valide : Alice envoie 200 à Bob
        Transaction transaction1 = new Transaction(account1, account2, 200);
        transaction1.processTransaction();

        // Transaction valide : Bob envoie 50 à Alice
        Transaction transaction2 = new Transaction(account2, account1, 50);
        transaction2.processTransaction();

        // Transaction invalide : Bob tente d'envoyer plus d'argent qu'il n'a
        Transaction transaction3 = new Transaction(account2, account1, 600);
        transaction3.processTransaction();

        // Transaction invalide : Bob tente d'envoyer plus d'argent qu'il n'a
        Transaction transaction4 = new Transaction(account2, account1, 60000);
        transaction4.processTransaction(); // Cette transaction échouera et affichera un message d'erreur

        // Affichage de l'historique des transactions après exécution
        System.out.println("\n=== Historique des transactions après exécution ===");
        account1.displayTransactionHistory();
        account2.displayTransactionHistory();
    }
}
