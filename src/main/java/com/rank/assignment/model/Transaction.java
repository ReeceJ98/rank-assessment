package com.rank.assignment.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transaction")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transactionId")
    private int transactionId;

    @Column(name = "transactionType")
    private String transactionType;

    @Column(name = "amount")
    private double amount;

    @Column(name = "playerId")
    private long playerId;

    public Transaction(String transactionType, double amount, long playerId) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.playerId = playerId;
    }
}
