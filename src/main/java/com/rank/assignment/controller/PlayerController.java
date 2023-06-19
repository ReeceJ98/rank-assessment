package com.rank.assignment.controller;

import com.rank.assignment.dto.request.NewTransactionRequestDto;
import com.rank.assignment.dto.response.LastTenTransactionsResponseDto;
import com.rank.assignment.dto.response.NewPlayerResponseDto;
import com.rank.assignment.dto.response.NewTransactionResponseDto;
import com.rank.assignment.model.Player;
import com.rank.assignment.model.Transaction;
import com.rank.assignment.respository.PlayerRepository;
import com.rank.assignment.respository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/player")
public class PlayerController {

    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    TransactionRepository transactionRepository;

    @PostMapping("/new")
    public ResponseEntity<Player> createPlayer(@RequestBody Player player) {
        return new ResponseEntity<>(playerRepository.save(player), HttpStatus.OK);
    }

    @GetMapping("/player/{playerId}/balance")
    public ResponseEntity<NewPlayerResponseDto> getPlayer(@PathVariable("playerId") int playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (null == player) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        NewPlayerResponseDto newPlayerResponseDto = new NewPlayerResponseDto(player.getId(), player.getBalance());


        return new ResponseEntity<>(newPlayerResponseDto, HttpStatus.OK);
    }

    @PostMapping("/player/{playerId}/balance/update")
    public ResponseEntity<NewTransactionResponseDto> newTransaction(@PathVariable("playerId") int playerId, @RequestBody NewTransactionRequestDto newTransactionRequestDto) {

        double amount = newTransactionRequestDto.getAmount();
        String transactionType = newTransactionRequestDto.getTransactionType();

        if (amount < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player playerdata = playerRepository.findPlayerById(playerId);

        if (null == playerdata) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (transactionType.equals("win")) {
            double newbalance = playerdata.getBalance() + amount;
//            System.out.println("newbalance: [win] : " + newbalance);
            playerdata.setBalance(newbalance);
        } else if (transactionType.equals("wager")) {
            if (playerdata.getBalance() < amount) {
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }
            double newbalance = playerdata.getBalance() - amount;
//            System.out.println("newbalance: [wager] : " + newbalance);
            playerdata.setBalance(newbalance);
        }

        playerRepository.save(playerdata);
        Transaction newTransaction = transactionRepository.save(new Transaction(transactionType, amount, playerId));

        NewTransactionResponseDto newTransactionResponseDto = new NewTransactionResponseDto(newTransaction.getTransactionId(), playerdata.getBalance());

        return new ResponseEntity<>(newTransactionResponseDto, HttpStatus.OK);
    }

    @PostMapping("/admin/player/transactions")
    public ResponseEntity<List<LastTenTransactionsResponseDto>> getLastTenTransactions(@RequestParam() String userName) {
        try {

            List<Transaction> transactionList = new ArrayList<>();

            Player player = playerRepository.findByUsername(userName);
            if (null == player) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            transactionList = transactionRepository.findLastTenTransactions(player.getId());

            if (transactionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {

                return new ResponseEntity<>(getLastTenTransactionsResponseDtos(transactionList), HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private List<LastTenTransactionsResponseDto> getLastTenTransactionsResponseDtos(List<Transaction> transactionList) {
        List<LastTenTransactionsResponseDto> dtos = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            dtos.add(new LastTenTransactionsResponseDto(transaction.getTransactionId(), transaction.getTransactionType(), transaction.getAmount()));
        }
        return dtos;
    }

}
