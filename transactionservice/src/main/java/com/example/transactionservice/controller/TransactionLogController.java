package com.example.transactionservice.controller;

import com.example.transactionservice.model.TransactionLog;
import com.example.transactionservice.service.TransactionLogService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionLogController {

    private final TransactionLogService logService;

    public TransactionLogController(TransactionLogService logService) {
        this.logService = logService;
    }

    @GetMapping("/all")
    public List<TransactionLog> all() {
        return logService.all();
    }

    @GetMapping("/authorized")
    public List<TransactionLog> authorized() {
        return logService.authorized();
    }

    @GetMapping("/blocked")
    public List<TransactionLog> blocked() {
        return logService.blocked();
    }
}
