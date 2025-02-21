package com.corebanker.models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
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

        // Charger les transactions sauvegardées
        loadTransactionHistory();
    }

    /**
     * Ajoute une transaction à l'historique du compte.
     * @param transaction La transaction à ajouter.
     */
    public void addTransactionToHistory(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    /**
     * Charge l’historique des transactions depuis le fichier transactions.log
     */
    private void loadTransactionHistory() {
        try (BufferedReader br = new BufferedReader(new FileReader("transactions.log"))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Chaque ligne du fichier log est sous la forme : transactionId,timestamp,source,target,amount
                String[] data = line.split(",");

                if (data.length == 5) {
                    String transactionId = data[0];  // ID de transaction
                    String timestamp = data[1];      // Date de la transaction
                    String sourceOwner = data[2];    // Propriétaire du compte source
                    String targetOwner = data[3];    // Propriétaire du compte cible
                    double amount = Double.parseDouble(data[4]); // Montant de la transaction

                    // Vérifie si cette transaction concerne CE compte
                    BankAccount source = this.owner.equals(sourceOwner) ? this : null;
                    BankAccount target = this.owner.equals(targetOwner) ? this : null;

                    if (source != null || target != null) {
                        // Utilise la méthode fromLog pour recréer la transaction
                        Transaction transaction = Transaction.fromLog(transactionId, timestamp, sourceOwner, targetOwner, amount);
                        if (transaction != null) {
                            this.transactionHistory.add(transaction);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Aucune transaction enregistrée (fichier transactions.log introuvable).");
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'historique des transactions : " + e.getMessage());
        }
    }

    /**
     * Affiche l'historique des transactions du compte.
     */
    public void displayTransactionHistory() {
        System.out.println("\n=== Historique des transactions pour " + owner + " ===");

        for (Transaction transaction : transactionHistory) {
            if (transaction == null) {  // Ajoute cette vérification pour éviter NullPointerException
                System.out.println("⚠️ Une transaction invalide a été ignorée.");
                continue;
            }
            transaction.displayTransactionDetails();
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
