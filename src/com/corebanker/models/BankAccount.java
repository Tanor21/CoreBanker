package com.corebanker.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankAccount {
    private final String accountNumber;
    private final String owner;
    private double balance;
    private List<Transaction> transactionHistory; // Historique des transactions

    /**
     * Constructeur du compte bancaire
     * @param owner Nom du propriétaire
     * @param initialBalance Solde initial du compte
     */
    public BankAccount(String owner, double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.owner = owner;
        this.balance = initialBalance;
        this.transactionHistory = new ArrayList<>(); // Initialisation de l'historique
    }

    /**
     * Ajoute une transaction à l'historique du compte.
     * @param transaction La transaction à ajouter.
     */
    public void addTransactionToHistory(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Affiche l'historique des transactions du compte.
     */
    public void displayTransactionHistory() {
        System.out.println("\n=== Historique des transactions pour " + owner + " ===");
        if (transactionHistory.isEmpty()) {
            System.out.println("Aucune transaction enregistrée.");
        } else {
            for (Transaction transaction : transactionHistory) {
                transaction.displayTransactionDetails();
            }
        }
    }

    /**
     * Génère un numéro de compte unique au format CB-XXXXXXXX.
     */
    private String generateAccountNumber() {
        return "CB-" + UUID.randomUUID().toString().substring(0, 8);
    }


    /**
     * Dépose un montant sur le compte.
     * @param amount Montant à déposer (doit être positif).
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Dépôt de " + amount + " réussi. Nouveau solde : " + balance);
        } else {
            System.out.println("Le montant du dépôt doit être positif.");
        }
    }


    /**
     * Retire un montant du compte si le solde est suffisant.
     * @param amount Montant à retirer.
     * @return true si le retrait a réussi, false sinon.
     */
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Retrait de " + amount + " réussi. Nouveau solde : " + balance);
            return true;
        } else {
            System.out.println("Fonds insuffisants ou montant invalide.");
            return false;
        }
    }


    // Méthode pour récupérer le numéro de compte, utilisée plus tard
    public String getAccountNumber() {
        return accountNumber;
    }

    public String getOwner() {
        return owner;
    }

    public double getBalance() {
        return balance;
    }


    /**
     * Affiche les détails du compte bancaire.
     */
    public void displayAccountDetails() {
        System.out.println("Numéro de compte : " + accountNumber);
        System.out.println("Propriétaire : " + owner);
        System.out.println("Solde : " + balance);
    }



}
