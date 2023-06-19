package com.rank.assignment.respository;

import com.rank.assignment.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findPlayerById(int playerId);

    Player findByUsername(String playerName);

}

