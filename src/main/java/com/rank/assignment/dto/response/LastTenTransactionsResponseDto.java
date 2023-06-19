package com.rank.assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
@Getter
@AllArgsConstructor
public class LastTenTransactionsResponseDto implements Serializable {
    int transaction_Id;
    String transaction_type;
    double amount;
}
