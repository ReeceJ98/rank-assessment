package com.rank.assignment.controller;

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
    public ResponseEntity<Map> getPlayer(@PathVariable("playerId") int playerId) {
        Player player = playerRepository.findPlayerById(playerId);
        if (null == player) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("playerId", player.getId());
        responseMap.put("balance", player.getBalance());


        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/player/{playerId}/balance/update")
    public ResponseEntity<Map> newTransaction(@PathVariable("playerId") int playerId, @RequestBody double amount, @RequestBody String transactionType) {

        if (amount < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Player playerdata = playerRepository.findPlayerById(playerId);

        if (null == playerdata) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (transactionType.equals("win")) {
            double newbalance = playerdata.getBalance() + amount;
            System.out.println("newbalance: [win] : " + newbalance);
            playerdata.setBalance(newbalance);
        } else if (transactionType.equals("wager")) {
            if (playerdata.getBalance() < amount) {
                return new ResponseEntity<>(HttpStatus.I_AM_A_TEAPOT);
            }
            double newbalance = playerdata.getBalance() - amount;
            System.out.println("newbalance: [wager] : " + newbalance);
            playerdata.setBalance(newbalance);
        }

        playerRepository.save(playerdata);
        Transaction newTransaction = transactionRepository.save(new Transaction(0, transactionType, amount, playerId));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("transactionId", newTransaction.getTransactionId());
        responseMap.put("balance", playerdata.getBalance());

        return new ResponseEntity<>(responseMap, HttpStatus.OK);
    }

    @PostMapping("/admin/player/transactions")
    public ResponseEntity<List<Map>> getLastTenTransactions(@RequestParam() String name) {
        try {

            List<Map> transactionData = new ArrayList<>();
            List<Transaction> transactionList = new ArrayList<>();

            Player player = playerRepository.findByUsername(name);
            if (null == player) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            transactionList = transactionRepository.findLastTenTransactions(player.getId());

            if (transactionList.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                for (Transaction transaction : transactionList) {
                    transactionData.add(convertTransactionToMap(transaction));
                }

                return new ResponseEntity<>(transactionData, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private Map<String, Object> convertTransactionToMap(Transaction transaction) {
        Map<String, Object> map = new HashMap<>();
        map.put("transactionId", transaction.getTransactionId());
        map.put("transactionType", transaction.getTransactionType());
        map.put("amount", transaction.getAmount());
        return map;
    }

}
