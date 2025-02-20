package com.corebanker.models;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
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
     * Journalise l'événement de la transaction dans un fichier de log.
     * @param message Le message à enregistrer dans le log.
     */
    private void logTransaction(String message) {
        try (FileWriter fw = new FileWriter("transaction_log.txt", true);
             PrintWriter out = new PrintWriter(fw)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
            out.println(timestamp + " - " + message);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans le fichier de log.");
        }
    }

    /**
     * Exécute la transaction entre les comptes
     */
    /**
     * Modifie la méthode processTransaction() pour inclure la journalisation
     */
    public void processTransaction() {
        // Vérifier si le montant est valide (positif)
        if (amount <= 0) {
            logTransaction("Échec de la transaction " + transactionId + " : Montant invalide.");
            System.out.println("Erreur : Montant de transaction invalide.");
            return; // On arrête ici si le montant est invalide
        }

        // Vérifier si le solde du compte source est suffisant
        if (sourceAccount.getBalance() < amount) {
            logTransaction("Échec de la transaction " + transactionId + " : Fonds insuffisants.");
            System.out.println("Échec de la transaction : fonds insuffisants sur le compte de " + sourceAccount.getOwner());
            return; // On arrête ici si le solde est insuffisant
        }

        // Effectuer la transaction
        sourceAccount.withdraw(amount); // Débiter le compte source
        targetAccount.deposit(amount);  // Créditer le compte cible

        // Ajouter la transaction à l’historique des comptes concernés
        sourceAccount.addTransactionToHistory(this);
        sourceAccount.addTransactionToHistory(this);

        // Journaliser la transaction réussie
        logTransaction("Transaction réussie. ID: " + transactionId + " de " + sourceAccount.getOwner() + " à " + targetAccount.getOwner() + " pour " + amount + "€.");
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
