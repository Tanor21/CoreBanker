package com.corebanker.models;

import com.corebanker.enums.TransactionStatus;
import com.corebanker.enums.TransactionType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BankAccount {
    private final String accountNumber;
    private final String owner;
    private double balance;
    private final List<Transaction> transactionHistory; // Historique des transactions

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
                // Chaque ligne du fichier log est sous la forme : transactionId, timestamp, source, target, amount, type, status
                String[] data = line.split(",");

                if (data.length == 7) {
                    String transactionId = data[0];  // ID de transaction
                    String timestamp = data[1];      // Date de la transaction
                    String sourceOwner = data[2];    // Propriétaire du compte source
                    String targetOwner = data[3];    // Propriétaire du compte cible
                    double amount = Double.parseDouble(data[4]); // Montant de la transaction
                    TransactionType type = TransactionType.valueOf(data[5].toUpperCase()); // Type de la transaction (ex: TRANSFER)
                    TransactionStatus status = TransactionStatus.valueOf(data[6].toUpperCase()); // Statut de la transaction (ex: PENDING)

                    // Vérifie si cette transaction concerne CE compte
                    BankAccount source = this.owner.equals(sourceOwner) ? this : null;
                    BankAccount target = this.owner.equals(targetOwner) ? this : null;

                    if (source != null || target != null) {
                        // Utilise la méthode fromLog pour recréer la transaction
                        Transaction transaction = Transaction.fromLog(transactionId, timestamp, sourceOwner, targetOwner, amount, type, status);
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
        System.out.println("\n=== 📜 Historique des transactions pour " + owner + " 📜 ===");

        if (transactionHistory == null || transactionHistory.isEmpty()) {
            System.out.println("⚠️ Aucun historique de transaction disponible.");
            return;
        }

        for (Transaction transaction : transactionHistory) {
            System.out.println("══════════════════════════════════════════════════");
            System.out.println("🆔 ID de transaction : " + transaction.getTransactionId());
            System.out.println("📅 Date et Heure    : " + transaction.getTransactionDate());
            System.out.println("📤 Expéditeur       : " + transaction.getSourceOwner() + " (" + transaction.getSourceAccount().getAccountNumber() + ")");
            System.out.println("📥 Destinataire     : " + transaction.getTargetOwner() + " (" + transaction.getTargetAccount().getAccountNumber() + ")");
            System.out.println("💰 Montant Transféré: " + transaction.getAmount() + " €");
            System.out.println("══════════════════════════════════════════════════");
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
        // Vérification que le montant est positif
        if (amount <= 0) {
            System.out.println("Erreur : Le montant du dépôt doit être positif.");
        } else {
            // Si tout est ok, on ajoute le montant au solde du compte
            balance += amount;
            System.out.println("Dépôt de " + amount + " € réussi. Nouveau solde : " + balance);
        }
    }

    /**
     * Retire un montant du compte si le solde est suffisant.
     * @param amount Montant à retirer.
     * @return true si le retrait a réussi, false sinon.
     */
    public boolean withdraw(double amount) {
        // Vérification que le montant est positif
        if (amount <= 0) {
            System.out.println("Erreur : Le montant du retrait doit être positif.");
            return false; // On retourne false si le montant est invalide
        }

        // Vérification que le solde est suffisant
        if (amount > balance) {
            System.out.println("Erreur : Fonds insuffisants.");
            return false; // On retourne false si le solde est insuffisant
        }

        // Si tout est ok, on effectue le retrait et on met à jour le solde
        balance -= amount;
        System.out.println("Retrait de " + amount + " € réussi. Nouveau solde : " + balance);
        return true; // La transaction a réussi, donc on retourne true
    }

    /**
     * Calcule les frais de transaction.
     * Si c'est un transfert, applique 2% de frais.
     * @param amount Montant de la transaction
     * @param type Type de la transaction (par exemple, TRANSFER)
     * @return Montant des frais à appliquer
     */
    public double calculateTransactionFee(double amount, TransactionType type) {
        // Si c'est un transfert, on applique 2% de frais
        if (type == TransactionType.TRANSFER) {
            return amount * 0.02; // 2% du montant
        }
        return 0; // Pas de frais pour les autres types de transactions
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
