import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

class Account {
    private int accountNumber;
    private double balance;

    public Account(int accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Deposit successful. Current balance: " + balance);
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Withdrawal successful. Current balance: " + balance);
        } else if (amount > balance) {
            System.out.println("Insufficient balance.");
        } else {
            System.out.println("Withdrawal amount must be positive.");
        }
    }

    public void checkBalance() {
        System.out.println("Current balance: " + balance);
    }
}

class Bank {
    private Map<Integer, Account> accounts = new HashMap<>();

    public Account createAccount(int accountNumber) {
        if (accounts.containsKey(accountNumber)) {
            System.out.println("Account number already exists. Please choose a different number.");
            return null;
        }
        Account newAccount = new Account(accountNumber);
        accounts.put(accountNumber, newAccount);
        System.out.println("Account created successfully. Account number: " + accountNumber);
        return newAccount;
    }

    public Account getAccount(int accountNumber) {
        return accounts.get(accountNumber);
    }
}

public class BankAccountManagementSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bank Account Management System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter new account number: ");
                    int newAccountNumber = scanner.nextInt();
                    bank.createAccount(newAccountNumber);
                    break;
                case 2:
                    System.out.print("Enter account number: ");
                    int accNumberDeposit = scanner.nextInt();
                    Account accDeposit = bank.getAccount(accNumberDeposit);
                    if (accDeposit != null) {
                        System.out.print("Enter amount to deposit: ");
                        double amountDeposit = scanner.nextDouble();
                        accDeposit.deposit(amountDeposit);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    int accNumberWithdraw = scanner.nextInt();
                    Account accWithdraw = bank.getAccount(accNumberWithdraw);
                    if (accWithdraw != null) {
                        System.out.print("Enter amount to withdraw: ");
                        double amountWithdraw = scanner.nextDouble();
                        accWithdraw.withdraw(amountWithdraw);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter account number: ");
                    int accNumberBalance = scanner.nextInt();
                    Account accBalance = bank.getAccount(accNumberBalance);
                    if (accBalance != null) {
                        accBalance.checkBalance();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 5:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}