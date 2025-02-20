package com.corebanker.models;

import java.util.UUID;

public class BankAccount {
    private final String accountNumber;
    private final String owner;
    private double balance;

    /**
     * Constructeur : initialise un compte bancaire avec un propriétaire et un solde initial.
     * Un numéro de compte unique est généré automatiquement.
     */
    public BankAccount(String owner, double initialBalance) {
        this.accountNumber = generateAccountNumber();
        this.owner = owner;
        this.balance = initialBalance;
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
