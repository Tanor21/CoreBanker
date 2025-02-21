package com.corebanker.managers;

import com.corebanker.models.BankAccount;
import java.util.ArrayList;
import java.util.List;

public class BankAccountManager {
    private static List<BankAccount> accounts = new ArrayList<>();

    // Méthode pour ajouter un compte
    public static void addAccount(BankAccount account) {
        if (account != null) {
            accounts.add(account);
            System.out.println("Compte ajouté : " + account.getAccountNumber());
        } else {
            System.out.println("Erreur : Compte invalide.");
        }
    }

    // Méthode pour rechercher un compte par son numéro
    public static BankAccount findAccountByNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isEmpty()) {
            System.out.println("Erreur : Numéro de compte invalide.");
            return null;
        }

        for (BankAccount account : accounts) {
            System.out.println("Recherche du compte : " + account.getAccountNumber());
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }

        System.out.println("Compte non trouvé : " + accountNumber);
        return null; // Retourne null si le compte n'est pas trouvé
    }
}
