package com.example.transactionservice.service;

import com.example.transactionservice.model.TransactionLog;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class TransactionLogService {

    private final List<TransactionLog> logs = new CopyOnWriteArrayList<>();

    public void add(TransactionLog log) {
        logs.add(log);
    }

    public List<TransactionLog> all() {
        return logs;
    }

    public List<TransactionLog> authorized() {
        return logs.stream().filter(l -> "SUCCESS".equalsIgnoreCase(l.getStatus())).toList();
    }

    public List<TransactionLog> blocked() {
        return logs.stream().filter(l -> "BLOCKED".equalsIgnoreCase(l.getStatus())).toList();
    }
}
