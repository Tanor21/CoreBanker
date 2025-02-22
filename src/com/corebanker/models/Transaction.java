package com.corebanker.models;

import com.corebanker.enums.TransactionStatus;
import com.corebanker.enums.TransactionType;
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
    private final BankAccount sourceAccount; // Compte source
    private final BankAccount targetAccount; // Compte cible
    private final double amount; // Montant de la transaction
    private Date transactionDate; // Date de la transaction
    private TransactionType transactionType; // // Nouveau champ pour le type de la transaction
    private TransactionStatus status; // Nouveau champ pour le statut de la transaction

    /**
     * Constructeur de la classe Transaction
     * @param sourceAccount Le compte source
     * @param targetAccount Le compte cible
     * @param amount Le montant à transférer
     */
    public Transaction(BankAccount sourceAccount, BankAccount targetAccount, double amount, TransactionType transactionType, TransactionStatus status) {
        this.transactionId = UUID.randomUUID().toString(); // Génère un ID unique pour la transaction
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.transactionDate = new Date(); // Capture la date et l'heure actuelles
        this.transactionType = transactionType;
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public BankAccount getSourceAccount() {
        return sourceAccount;
    }

    public BankAccount getTargetAccount() {
        return targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public String getTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transactionDate);
    }

    public String getSourceOwner() {
        return sourceAccount != null ? sourceAccount.getOwner() : "Compte source inconnu";
    }

    public String getTargetOwner() {
        return targetAccount != null ? targetAccount.getOwner() : "Compte cible inconnu";
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }


    /**
     * Enregistre la transaction dans le fichier transactions.log
     */
    private void saveTransactionToFile() {
        try (FileWriter fw = new FileWriter("transactions.log", true);
             PrintWriter out = new PrintWriter(fw)) {

            // Formatage de la date
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transactionDate);

            // Informations supplémentaires
            String sourceOwner = sourceAccount.getOwner();
            String targetOwner = targetAccount.getOwner();
            String transactionType = this.transactionType.name(); // Utiliser `name()` pour l'enum
            String status = this.status.name(); // Utiliser `name()` pour l'enum

            // Ligne formatée avec des éléments visuels
            String logEntry = "🆔 Transaction ID | " + transactionId + "\n" +
                    "📅 Date & Heure   | " + timestamp + "\n" +
                    "📤 Expéditeur     | " + sourceAccount.getAccountNumber() + " (" + sourceOwner + ")\n" +
                    "📥 Destinataire   | " + targetAccount.getAccountNumber() + " (" + targetOwner + ")\n" +
                    "💰 Montant        | " + amount + " €\n" +
                    "🔄 Type de Trans.  | " + transactionType + "\n" +
                    "✔️ Statut         | " + status + "\n";

            // Ajout d'un séparateur pour la lisibilité
            out.println("══════════════════════════════════════════════════════════════════════════════════════════════════════");
            out.println(logEntry);
            out.println("══════════════════════════════════════════════════════════════════════════════════════════════════════");
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
            System.out.println("❌ Erreur : Montant de transaction invalide.");
            return false;
        }

        // Vérifier si le solde du compte source est suffisant
        if (sourceAccount.getBalance() < amount) {
            System.out.println("❌ Échec de la transaction : fonds insuffisants sur le compte de " + sourceAccount.getOwner());
            return false;
        }

        return true; // Toutes les validations sont passées
    }

    /**
     * Exécute la transaction entre les comptes après validation.
     */
    public boolean processTransaction() {
        if (!validateTransaction()) {
            System.out.println("❌ La transaction a été annulée.");
            this.status = TransactionStatus.FAILED; // ❌ Échec de la transaction
            return false; // 🔴 Indique un échec
        }

        double fee = sourceAccount.calculateTransactionFee(amount, transactionType);
        double amountAfterFee = amount - fee;

        if (transactionType == TransactionType.TRANSFER) {
            System.out.println("Frais de transaction : " + fee + " €.");
            System.out.println("Montant après frais : " + amountAfterFee + " €.");

            if (sourceAccount.withdraw(amount)) {
                targetAccount.deposit(amountAfterFee);
                System.out.println("Transaction réussie. ID: " + transactionId);
                this.status = TransactionStatus.SUCCEEDED; // ✅ Succès de la transaction
                sourceAccount.addTransactionToHistory(this);
                targetAccount.addTransactionToHistory(this);
                saveTransactionToFile();
                return true; // 🟢 Indique un succès
            } else {
                System.out.println("❌ Erreur : Fonds insuffisants pour effectuer la transaction.");
                this.status = TransactionStatus.FAILED; // ❌ Échec de la transaction
                return false; // 🔴 Indique un échec
            }
        }

        return false; // 🔴 Par défaut, on retourne false si le type de transaction n'est pas pris en charge
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
    public static Transaction fromLog(String transactionId, String timestamp, String sourceAccountNumber, String targetAccountNumber, double amount, TransactionType type, TransactionStatus status) {
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
        Transaction transaction = new Transaction(sourceAccount, targetAccount, amount, type, status);
        transaction.transactionId = transactionId;

        // Parse la date du log
        try {
            transaction.transactionDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp);
        } catch (ParseException e) {
            System.out.println("Erreur de format de date dans les logs : " + e.getMessage());
        }

        return transaction;
    }


}
