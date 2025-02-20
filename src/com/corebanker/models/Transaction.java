package com.corebanker.models;

import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId; // Identifiant unique
    private BankAccount sourceAccount; // Compte source
    private BankAccount targetAccount; // Compte cible
    private double amount; // Montant de la transaction
    private Date transactionDate; // Date de la transaction


    /**
     * Constructeur de la classe Transaction
     * @param sourceAccount Le compte source
     * @param targetAccount Le compte cible
     * @param amount Le montant à transférer
     */
    public Transaction(BankAccount sourceAccount, BankAccount targetAccount, double amount) {
        this.transactionId = UUID.randomUUID().toString(); // Génération d'un identifiant unique
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.transactionDate = new Date();
    }

    /**
     * Exécute la transaction entre les comptes
     */
    public void processTransaction() {
        // Vérification du montant valide
        if (amount <= 0) {
            System.out.println("Erreur : Montant de transaction invalide.");
            return; // On arrête ici si le montant est invalide
        }

        // Vérification du solde disponible
        if (sourceAccount.getBalance() < amount) {
            System.out.println("Échec de la transaction : fonds insuffisants sur le compte de " + sourceAccount.getOwner());
            return; // On arrête ici si le solde est insuffisant
        }

        // Débiter le compte source et créditer le compte cible
        sourceAccount.withdraw(amount); // Retirer le montant du compte source
        targetAccount.deposit(amount);  // Ajouter le montant au compte cible

        // Afficher que la transaction a réussi
        System.out.println("Transaction réussie. ID: " + transactionId);
    }


    /**
     * Affiche les détails de la transaction
     */
    public void displayTransactionDetails() {
        System.out.println("=== Transaction Details ===");
        System.out.println("ID: " + transactionId);
        System.out.println("Source: " + sourceAccount.getOwner());
        System.out.println("Cible: " + targetAccount.getOwner());
        System.out.println("Montant: " + amount);
        System.out.println("Date: " + transactionDate);
    }


}
