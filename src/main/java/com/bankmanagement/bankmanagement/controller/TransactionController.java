package com.bankmanagement.bankmanagement.controller;

import com.bankmanagement.bankmanagement.helper.status.TransactionType;
import com.bankmanagement.bankmanagement.helper.status.TransactionStatus;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.Transaction;
import com.bankmanagement.bankmanagement.model.User;
import com.bankmanagement.bankmanagement.service.serviceClass.TransactionService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class TransactionController implements Initializable {

    @FXML
    private Label accountNumberLabel;

    @FXML
    private Label accountStatusLabel;

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private Label lastUpdated;

    @FXML
    private Label transactionAmount;

    @FXML
    private Label transactionDate;

    @FXML
    private Label transactionIdLabel;

    @FXML
    private TableColumn<Transaction, Long> id;

    @FXML
    private Label transactionStatusLabel;

    @FXML
    private Label transactionTypeLabel;

    @FXML
    private TableColumn<Transaction, TransactionStatus> transactionStatus;

    @FXML
    private TableColumn<Transaction, TransactionType> transactionType;

    @FXML
    private TextField depoAccountNumber;

    @FXML
    private TextField depoAmount;

    @FXML
    private TextField depoReason;

    @FXML
    private TextField depositor;

    @FXML
    private Label depositStatusMsg;

    @FXML
    private TextField withAccountNumber;

    @FXML
    private TextField withAmount;

    @FXML
    private TextField withReason;

    @FXML
    private Label withSuccessMsg;

    @FXML
    private TextField withdrawer;

    private final TransactionService transactionService = new TransactionService();

    private final ObservableList<Transaction> data = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.id.setCellValueFactory(new PropertyValueFactory<Transaction, Long>("id"));
        this.transactionStatus.setCellValueFactory(new PropertyValueFactory<Transaction, TransactionStatus>("transactionStatus"));
        this.transactionType.setCellValueFactory(new PropertyValueFactory<Transaction, TransactionType>("transactionType"));
        this.transactionTable.getColumns().addAll(id, transactionStatus, transactionType);

        List<Transaction> transactions = transactionService.getTransactions();

        if(!transactions.isEmpty()) data.addAll(transactions);
        transactionTable.setItems(data);
    }

    @FXML
    void ShowTransactionDetails(MouseEvent event) {
        Transaction selectedTransaction = transactionTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction != null) {
            accountNumberLabel.setText(selectedTransaction.getAccount().getAccountNumber());
            accountStatusLabel.setText(selectedTransaction.getAccount().getAccountStatus().toString());
            lastUpdated.setText(new Date().toString());
            transactionIdLabel.setText(selectedTransaction.getId().toString());
            transactionTypeLabel.setText(selectedTransaction.getTransactionType().toString());
            transactionAmount.setText(selectedTransaction.getAmount().toString());
            transactionDate.setText(selectedTransaction.getTransactionDate().toString());
            transactionStatusLabel.setText(selectedTransaction.getTransactionStatus().toString());
        }

    }
    @FXML
    void makeDeposit(MouseEvent event) {
        Account account = transactionService.findAccountBy("AccountNumber", depoAccountNumber.getText());
        account.setBalance(account.getBalance() + Double.parseDouble(depoAmount.getText()));

        User user = account.getAccountHolder();
        Transaction transaction = new Transaction(TransactionType.DEPOSIT, account, Double.parseDouble(depoAmount.getText()), 2344, user, depoReason.getText());
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        transactionService.addTransaction(transaction);

        transactionService.updateAccount(account);

        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        transactions.add(transaction);

        transactionTable.getItems().add(transaction);

        depoAccountNumber.setText("");
        depoAmount.setText("");
        depositor.setText("");
        depoReason.setText("");
        depositStatusMsg.setText("Deposit Successfully!!");
    }

    @FXML
    void makeWithdrawal(MouseEvent event) {
        Account account = transactionService.findAccountBy("AccountNumber", withAccountNumber.getText());
        account.setBalance(account.getBalance() - Double.parseDouble(withAmount.getText()));

        User user = account.getAccountHolder();
        Transaction transaction = new Transaction(TransactionType.WITHDRAW, account, Double.parseDouble(withAmount.getText()), 2344, user, withReason.getText());
        transaction.setTransactionStatus(TransactionStatus.PENDING);
        transactionService.addTransaction(transaction);

        transactionService.updateAccount(account);

        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        transactions.add(transaction);

        transactionTable.getItems().add(transaction);

        withAmount.setText("");
        withdrawer.setText("");
        withReason.setText("");
        withAccountNumber.setText("");
        withSuccessMsg.setText("Withdrawal Successful!!");
    }

}
