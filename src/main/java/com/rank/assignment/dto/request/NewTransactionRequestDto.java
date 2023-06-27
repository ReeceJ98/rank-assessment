package com.rank.assignment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewTransactionRequestDto implements Serializable {
    String transactionType;
    double amount;
}
