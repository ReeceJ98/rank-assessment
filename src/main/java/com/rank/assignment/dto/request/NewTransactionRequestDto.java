package com.rank.assignment.dto.request;

import lombok.Getter;
import java.io.Serializable;

@Getter
public class NewTransactionRequestDto implements Serializable {
    String transactionType;
    double amount;
}
