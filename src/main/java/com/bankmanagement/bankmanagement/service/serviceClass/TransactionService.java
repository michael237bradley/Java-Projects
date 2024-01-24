package com.bankmanagement.bankmanagement.service.serviceClass;

import com.bankmanagement.bankmanagement.dao.persistentDao.AccountDao;
import com.bankmanagement.bankmanagement.dao.persistentDao.TransactionDao;
import com.bankmanagement.bankmanagement.dao.persistentinit.BankManagementPersistentDao;
import com.bankmanagement.bankmanagement.model.Account;
import com.bankmanagement.bankmanagement.model.Transaction;

import java.util.List;

public class TransactionService {

    private final TransactionDao transactionDao;
    private AccountDao accountDao;

//    private final BankManagementPersistentDao bankManagementPersistentDao = new BankManagementPersistentDao();

    public TransactionService(){
        this.transactionDao = new TransactionDao();
        this.accountDao = new AccountDao();
    }
    public TransactionService(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public List<Transaction> getTransactions(){
        return transactionDao.findAll();
    }

    public Transaction getTransactionById(Long selectedTransactionId) {
        return transactionDao.findById(selectedTransactionId).orElse(null);
    }

    public Transaction addTransaction(Transaction transaction) {
        return transactionDao.add(transaction);
    }

    public Account findAccountBy(String accountNumber, String text) {
        return accountDao.findBy(accountNumber, text);
    }

    public Account updateAccount(Account account) {
        return accountDao.update(account);
    }
}
