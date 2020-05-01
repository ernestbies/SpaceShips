package com.shipsgame.domain.repository;

import com.shipsgame.domain.entity.Games;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GamesRepository extends JpaRepository<Games, String>, CrudRepository<Games, String> {

    @Query(value = "SELECT g FROM Games g WHERE g.login = :login")
    Games findGamesByLogin(@Param("login") String login);

}
