import java.util.*;
import java.util.concurrent.*;
import java.text.SimpleDateFormat;

class Account {
    private int accountNumber;
    private Map<String, Double> balances;
    private double lowBalanceThreshold = 100.0;
    private List<String> alerts;
    private List<ScheduledTransaction> scheduledTransactions;

    public Account(int accountNumber) {
        this.accountNumber = accountNumber;
        this.balances = new HashMap<>();
        this.alerts = new ArrayList<>();
        this.scheduledTransactions = new ArrayList<>();
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance(String currency) {
        return balances.getOrDefault(currency, 0.0);
    }

    public void deposit(String currency, double amount) {
        if (amount > 0) {
            balances.put(currency, getBalance(currency) + amount);
            System.out.println("Deposit successful. Current balance (" + currency + "): " + getBalance(currency));
            checkAlerts();
        } else {
            System.out.println("Deposit amount must be positive.");
        }
    }

    public void withdraw(String currency, double amount) {
        if (amount > 0 && amount <= getBalance(currency)) {
            balances.put(currency, getBalance(currency) - amount);
            System.out.println("Withdrawal successful. Current balance (" + currency + "): " + getBalance(currency));
            checkAlerts();
        } else if (amount > getBalance(currency)) {
            System.out.println("Insufficient balance.");
        } else {
            System.out.println("Withdrawal amount must be positive.");
        }
    }

    public void checkBalance(String currency) {
        System.out.println("Current balance (" + currency + "): " + getBalance(currency));
    }

    public void scheduleTransaction(String type, String currency, double amount, Date date) {
        ScheduledTransaction st = new ScheduledTransaction(type, currency, amount, date, this);
        scheduledTransactions.add(st);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        long delay = date.getTime() - System.currentTimeMillis();
        scheduler.schedule(st, delay, TimeUnit.MILLISECONDS);
        System.out.println("Transaction scheduled for: " + date);
    }

    public void checkAlerts() {
        for (String currency : balances.keySet()) {
            if (balances.get(currency) < lowBalanceThreshold) {
                String alert = "Alert: Low balance in " + currency + " account.";
                System.out.println(alert);
                alerts.add(alert);
            }
        }
    }

    public void showAlerts() {
        for (String alert : alerts) {
            System.out.println(alert);
        }
    }
}

class ScheduledTransaction implements Runnable {
    private String type;
    private String currency;
    private double amount;
    private Date date;
    private Account account;

    public ScheduledTransaction(String type, String currency, double amount, Date date, Account account) {
        this.type = type;
        this.currency = currency;
        this.amount = amount;
        this.date = date;
        this.account = account;
    }

    @Override
    public void run() {
        if (type.equals("deposit")) {
            account.deposit(currency, amount);
        } else if (type.equals("withdraw")) {
            account.withdraw(currency, amount);
        }
        System.out.println("Scheduled transaction completed on: " + date);
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

class CardManagement {
    private Map<Integer, String> cards = new HashMap<>();

    public void activateCard(int accountNumber) {
        cards.put(accountNumber, "Active");
        System.out.println("Card activated for account number: " + accountNumber);
    }

    public void blockCard(int accountNumber) {
        cards.put(accountNumber, "Blocked");
        System.out.println("Card blocked for account number: " + accountNumber);
    }

    public void orderReplacement(int accountNumber) {
        cards.put(accountNumber, "Replacement Ordered");
        System.out.println("Replacement card ordered for account number: " + accountNumber);
    }
}

class CustomerSupport {
    private Queue<String> supportTickets = new LinkedList<>();

    public void createSupportTicket(String issue) {
        supportTickets.add(issue);
        System.out.println("Support ticket created: " + issue);
    }

    public void showSupportTickets() {
        System.out.println("Support tickets:");
        for (String ticket : supportTickets) {
            System.out.println(ticket);
        }
    }
}

public class BankAccountManagementSystem {
    public static void main(String[] args) {
        Bank bank = new Bank();
        CardManagement cardManagement = new CardManagement();
        CustomerSupport customerSupport = new CustomerSupport();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Bank Account Management System ---");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Check Balance");
            System.out.println("5. Schedule Transaction");
            System.out.println("6. Manage Card");
            System.out.println("7. Customer Support");
            System.out.println("8. Show Alerts");
            System.out.println("9. Exit");
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
                        System.out.print("Enter currency: ");
                        String currencyDeposit = scanner.next();
                        System.out.print("Enter amount to deposit: ");
                        double amountDeposit = scanner.nextDouble();
                        accDeposit.deposit(currencyDeposit, amountDeposit);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter account number: ");
                    int accNumberWithdraw = scanner.nextInt();
                    Account accWithdraw = bank.getAccount(accNumberWithdraw);
                    if (accWithdraw != null) {
                        System.out.print("Enter currency: ");
                        String currencyWithdraw = scanner.next();
                        System.out.print("Enter amount to withdraw: ");
                        double amountWithdraw = scanner.nextDouble();
                        accWithdraw.withdraw(currencyWithdraw, amountWithdraw);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 4:
                    System.out.print("Enter account number: ");
                    int accNumberBalance = scanner.nextInt();
                    Account accBalance = bank.getAccount(accNumberBalance);
                    if (accBalance != null) {
                        System.out.print("Enter currency: ");
                        String currencyBalance = scanner.next();
                        accBalance.checkBalance(currencyBalance);
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 5:
                    System.out.print("Enter account number: ");
                    int accNumberSchedule = scanner.nextInt();
                    Account accSchedule = bank.getAccount(accNumberSchedule);
                    if (accSchedule != null) {
                        System.out.print("Enter transaction type (deposit/withdraw): ");
                        String type = scanner.next();
                        System.out.print("Enter currency: ");
                        String currencySchedule = scanner.next();
                        System.out.print("Enter amount: ");
                        double amountSchedule = scanner.nextDouble();
                        System.out.print("Enter date (yyyy-MM-dd HH:mm:ss): ");
                        scanner.nextLine();
                        String dateStr = scanner.nextLine();
                        try {
                            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
                            accSchedule.scheduleTransaction(type, currencySchedule, amountSchedule, date);
                        } catch (Exception e) {
                            System.out.println("Invalid date format.");
                        }
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 6:
                    System.out.print("Enter account number: ");
                    int accNumberCard = scanner.nextInt();
                    System.out.println("1. Activate Card");
                    System.out.println("2. Block Card");
                    System.out.println("3. Order Replacement");
                    int cardChoice = scanner.nextInt();
                    switch (cardChoice) {
                        case 1:
                            cardManagement.activateCard(accNumberCard);
                            break;
                        case 2:
                            cardManagement.blockCard(accNumberCard);
                            break;
                        case 3:
                            cardManagement.orderReplacement(accNumberCard);
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;
                case 7:
                    System.out.println("1. Create Support Ticket");
                    System.out.println("2. Show Support Tickets");
                    int supportChoice = scanner.nextInt();
                    scanner.nextLine();
                    switch (supportChoice) {
                        case 1:
                            System.out.print("Enter your issue: ");
                            String issue = scanner.nextLine();
                            customerSupport.createSupportTicket(issue);
                            break;
                        case 2:
                            customerSupport.showSupportTickets();
                            break;
                        default:
                            System.out.println("Invalid choice.");
                    }
                    break;
                case 8:
                    System.out.print("Enter account number: ");
                    int accNumberAlerts = scanner.nextInt();
                    Account accAlerts = bank.getAccount(accNumberAlerts);
                    if (accAlerts != null) {
                        accAlerts.showAlerts();
                    } else {
                        System.out.println("Account not found.");
                    }
                    break;
                case 9:
                    System.out.println("Exiting the system.");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice.");
                    break;
            }
        }
    }
}