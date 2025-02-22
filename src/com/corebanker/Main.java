package com.corebanker;

import com.corebanker.managers.BankAccountManager;
import com.corebanker.models.BankAccount;
import com.corebanker.models.Transaction;
import com.corebanker.enums.TransactionStatus;
import com.corebanker.enums.TransactionType;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================= Bienvenue dans l'application Core Banking =======================================");

        // Création de deux comptes bancaires avec des soldes initiaux
        BankAccount account1 = new BankAccount("Bob", 1000);
        BankAccount account2 = new BankAccount("Alice", 500);

        // Ajout des comptes au gestionnaire
        BankAccountManager.addAccount(account1);
        BankAccountManager.addAccount(account2);

        // Affichage des soldes initiaux des comptes
        System.out.println("\n=== Soldes initiaux ===");
        account1.displayAccountDetails();
        account2.displayAccountDetails();

        System.out.println("\n=== Exécution des transactions ===");

        // Transaction valide : Bob envoie 500€ à Alice
        executeTransaction(account1, account2, 500);

        // Transaction valide : Alice envoie 200€ à Bob
        executeTransaction(account2, account1, 200);

        // Transaction invalide : Alice tente d'envoyer 2000€ (fonds insuffisants)
        executeTransaction(account2, account1, 2000);

        // Affichage de l'historique des transactions après exécution
        System.out.println("\n=== Historique des transactions après exécution ===");
        account1.displayTransactionHistory();
        account2.displayTransactionHistory();
    }

    /**
     * Méthode utilitaire pour exécuter une transaction et afficher le résultat.
     */
    private static void executeTransaction(BankAccount source, BankAccount target, double amount) {
        System.out.println("---------------------------------------------------");
        System.out.println("🛠️ Nouvelle transaction : " + source.getOwner() + " → " + target.getOwner() + " | Montant : " + amount + "€");

        if (amount <= 0) {
            System.out.println("❌ Erreur : Le montant doit être supérieur à zéro.");
            return;
        }

        Transaction transaction = new Transaction(source, target, amount, TransactionType.TRANSFER, TransactionStatus.PENDING);

        if (transaction.processTransaction()) {  // ✅ Vérification du retour boolean
            System.out.println("✅ Transaction réussie ! ID: " + transaction.getTransactionId());
        } else {
            System.out.println("❌ Transaction échouée !");
        }

        source.displayAccountDetails();
        target.displayAccountDetails();
    }


}
