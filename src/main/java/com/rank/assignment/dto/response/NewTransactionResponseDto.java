package com.rank.assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
@AllArgsConstructor
public class NewTransactionResponseDto implements Serializable {
    int transactionId;
    double balance;
}
