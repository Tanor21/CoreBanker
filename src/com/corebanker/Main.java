package com.corebanker;

import com.corebanker.models.BankAccount;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================= Bienvenue dans l'application Core Banking =======================================");

        // Création d'un compte bancaire avec un solde initial de 1000
        BankAccount account1 = new BankAccount("Tanor", 1000.0);


        // Affichage des détails du compte
        System.out.println("Détails du compte");
        account1.displayAccountDetails();

        // Dépôt d'argent
        System.out.println("\nRetrait de 200...");
        account1.deposit(500);

        // Retrait d'argent
        System.out.println("\nWithdrawing 200...");
        account1.withdraw(200);

        // Tentative de retrait d'un montant supérieur au solde
        System.out.println("\nRetrait de 2000 (devrait échouer)... ");
        account1.withdraw(2000);

        // Affichage du solde final
        System.out.println("\n=== Détails finaux du compte ===");
        account1.displayAccountDetails();



    }
}