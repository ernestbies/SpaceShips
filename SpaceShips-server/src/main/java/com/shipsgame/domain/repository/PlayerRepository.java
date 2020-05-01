package com.shipsgame.domain.repository;

import com.shipsgame.domain.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;


@Repository
public interface PlayerRepository extends JpaRepository<Player, String>, CrudRepository<Player, String> {
    @Modifying
    @Transactional
    @Query(value = "UPDATE Player p SET p.bestScore = :bestScore WHERE p.login = :login AND (p.bestScore > :bestScore OR p.bestScore IS NULL)")
    void updateScore(@Param("login") String login, @Param("bestScore") Integer bestScore);
}
