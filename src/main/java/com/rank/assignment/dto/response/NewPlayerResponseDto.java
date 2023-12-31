package com.rank.assignment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class NewPlayerResponseDto implements Serializable {
    int playerId;
    double balance;
}
