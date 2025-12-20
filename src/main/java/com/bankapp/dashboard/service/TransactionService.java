package com.bankapp.dashboard.service;

import com.bankapp.dashboard.dto.AddMoneyRequest;
import com.bankapp.dashboard.dto.PayBillRequest;
import com.bankapp.dashboard.dto.SendMoneyRequest;
import com.bankapp.dashboard.model.*;
import com.bankapp.dashboard.repository.AccountRepository;
import com.bankapp.dashboard.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    // ==== LIST / FILTER ====

    public List<Transactions> searchForUser(
            String userId,
            LocalDate date,
            TransactionCategory category,
            Double minAmount,
            Double maxAmount
    ) {
        List<Transactions> all = transactionRepository.findByUserId(userId);

        return all.stream()
                .filter(t -> date == null || date.equals(t.getDate()))
                .filter(t -> category == null || category == t.getCategory())
                .filter(t -> minAmount == null || t.getAmount() >= minAmount)
                .filter(t -> maxAmount == null || t.getAmount() <= maxAmount)
                .toList();
    }

    // ==== COMMANDS ====

    // 1) ADD MONEY (CREDIT into one of my accounts)
    public Transactions addMoney(String userId, AddMoneyRequest req) {
        Accounts acc = accountRepository.findById(req.getAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        double current = acc.getBalance() != null ? acc.getBalance() : 0.0;
        acc.setBalance(current + req.getAmount());
        accountRepository.save(acc);

        Transactions tx = new Transactions();
        tx.setUserId(userId);
        tx.setAccountId(acc.getId());
        tx.setType(TransactionType.CREDIT);
        tx.setCategory(TransactionCategory.ADD_MONEY);
        tx.setAmount(req.getAmount());
        tx.setDescription(req.getNote());
        tx.setDate(LocalDate.now());

        return transactionRepository.save(tx);
    }

    // 2) SEND MONEY (DEBIT my account, CREDIT recipient account)
    public Transactions sendMoney(String userId, SendMoneyRequest req) {
        // Sender account
        Accounts from = accountRepository.findById(req.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Sender account not found"));

        // Recipient account by accountNumber
        List<Accounts> targets = accountRepository.findByAccountNumber(req.getToAccountNumber());
        if (targets.isEmpty()) {
            throw new IllegalArgumentException("Recipient account not found");
        }
        Accounts to = targets.get(0);

        double amount = req.getAmount();

        // Update balances
        double fromBal = from.getBalance() != null ? from.getBalance() : 0.0;
        if (fromBal < amount) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        from.setBalance(fromBal - amount);
        accountRepository.save(from);

        double toBal = to.getBalance() != null ? to.getBalance() : 0.0;
        to.setBalance(toBal + amount);
        accountRepository.save(to);

        // OUTGOING transaction for sender
        Transactions out = new Transactions();
        out.setUserId(userId);
        out.setAccountId(from.getId());
        out.setType(TransactionType.DEBIT);
        out.setCategory(TransactionCategory.SEND_MONEY);
        out.setAmount(amount);
        out.setDescription(req.getNote());
        out.setDate(LocalDate.now());
        out = transactionRepository.save(out);

        // INCOMING transaction for recipient
        Transactions in = new Transactions();
        in.setUserId(to.getUserId());
        in.setAccountId(to.getId());
        in.setType(TransactionType.CREDIT);
        in.setCategory(TransactionCategory.SEND_MONEY);
        in.setAmount(amount);
        in.setDescription("Received from another user");
        in.setDate(LocalDate.now());
        transactionRepository.save(in);

        // return sender side to frontend
        return out;
    }

    // 3) PAY BILL (DEBIT my account)
    public Transactions payBill(String userId, PayBillRequest req) {
        Accounts from = accountRepository.findById(req.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        double current = from.getBalance() != null ? from.getBalance() : 0.0;
        if (current < req.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance");
        }
        from.setBalance(current - req.getAmount());
        accountRepository.save(from);

        // convert string billerCode -> enum; fallback to CREDIT_CARD or throw
        BillType billType = BillType.valueOf(req.getBillerCode());

        Transactions tx = new Transactions();
        tx.setUserId(userId);
        tx.setAccountId(from.getId());
        tx.setType(TransactionType.DEBIT);
        tx.setCategory(TransactionCategory.PAY_BILL);
        tx.setBillType(billType);                    // <-- here
        tx.setAmount(req.getAmount());
        tx.setDescription(req.getNote());
        tx.setDate(LocalDate.now());

        return transactionRepository.save(tx);
    }


    // admin/internal if you still need
    public List<Transactions> getAll() {
        return transactionRepository.findAll();
    }
}
