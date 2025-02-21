package com.corebanker.models;

import com.corebanker.managers.BankAccountManager;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
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
        this.transactionId = UUID.randomUUID().toString(); // Génère un ID unique pour la transaction
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.transactionDate = new Date(); // Capture la date et l'heure actuelles
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    /**
     * Enregistre la transaction dans le fichier transactions.log
     */
    private void saveTransactionToFile() {
        try (FileWriter fw = new FileWriter("transactions.log", true);
             PrintWriter out = new PrintWriter(fw)) {
            // Formatage de la date
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transactionDate);

            // Ligne formatée pour être stockée sous forme CSV
            String logEntry = transactionId + "," + timestamp + "," + sourceAccount.getAccountNumber() + "," + targetAccount.getAccountNumber() + "," + amount;

            // Écriture dans le fichier
            out.println(logEntry);
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture dans le fichier transactions.log.");
        }
    }

    /**
     * Vérifie les préconditions avant de traiter la transaction.
     * @return True si les conditions sont valides, sinon False.
     */
    private boolean validateTransaction() {
        // Vérifier si les comptes existent
        if (sourceAccount == null || targetAccount == null) {
            System.out.println("⚠️ Erreur : Impossible de retrouver un ou plusieurs comptes !");
            return false;
        }

        // Vérifier si le montant est valide (positif)
        if (amount <= 0) {
            System.out.println("Erreur : Montant de transaction invalide.");
            return false;
        }

        // Vérifier si le solde du compte source est suffisant
        if (sourceAccount.getBalance() < amount) {
            System.out.println("Échec de la transaction : fonds insuffisants sur le compte de " + sourceAccount.getOwner());
            saveTransactionToFile(); // Sauvegarder l'échec dans les logs
            return false;
        }

        return true; // Toutes les validations sont passées
    }

    /**
     * Exécute la transaction entre les comptes après validation.
     */
    public void processTransaction() {
        // Valider la transaction avant de l'exécuter
        if (!validateTransaction()) {
            return; // La transaction ne sera pas exécutée si les validations échouent
        }

        // Effectuer la transaction
        sourceAccount.withdraw(amount); // Débiter le compte source
        targetAccount.deposit(amount);  // Créditer le compte cible

        // Ajouter la transaction à l’historique des comptes concernés
        sourceAccount.addTransactionToHistory(this);
        targetAccount.addTransactionToHistory(this);

        // Affichage du succès de la transaction
        System.out.println("Transaction réussie. ID: " + transactionId);

        // Sauvegarder la transaction dans le fichier
        saveTransactionToFile();
    }

    /**
     * Méthode statique pour recréer une transaction depuis les logs.
     * @param transactionId L'identifiant unique de la transaction
     * @param timestamp La date de la transaction sous forme de texte
     * @param sourceAccountNumber Le numéro de compte de l'expéditeur
     * @param targetAccountNumber Le numéro de compte du destinataire
     * @param amount Le montant de la transaction
     * @return Une instance de Transaction reconstruite
     */
    public static Transaction fromLog(String transactionId, String timestamp, String sourceAccountNumber, String targetAccountNumber, double amount) {
        System.out.println("🔍 Recherche des comptes pour la transaction " + transactionId);
        System.out.println("   - Expéditeur : " + sourceAccountNumber);
        System.out.println("   - Destinataire : " + targetAccountNumber);

        // Recherche des comptes dans le gestionnaire
        BankAccount sourceAccount = BankAccountManager.findAccountByNumber(sourceAccountNumber);
        BankAccount targetAccount = BankAccountManager.findAccountByNumber(targetAccountNumber);

        if (sourceAccount == null || targetAccount == null) {
            System.out.println("⚠️ Erreur : Impossible de retrouver un ou plusieurs comptes !");
            return null;
        }

        // Créer la transaction à partir des informations du log
        Transaction transaction = new Transaction(sourceAccount, targetAccount, amount);
        transaction.transactionId = transactionId;

        // Parsee la date du log
        try {
            transaction.transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
        } catch (ParseException e) {
            System.out.println("Erreur de format de date dans les logs : " + e.getMessage());
        }

        return transaction;
    }

    /**
     * Affiche les détails de la transaction.
     */
    public void displayTransactionDetails() {
        System.out.println("=== Transaction Details ===");
        System.out.println("ID: " + transactionId);
        System.out.println("Date: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transactionDate));
        System.out.println("Montant: " + amount);

        // Vérifier si les comptes existent avant d'afficher leurs propriétaires
        System.out.println("Expéditeur: " + (sourceAccount != null ? sourceAccount.getOwner() : "Compte inconnu"));
        System.out.println("Destinataire: " + (targetAccount != null ? targetAccount.getOwner() : "Compte inconnu"));

        System.out.println("===========================");
    }
}
