package com.shipsgame.domain.repository;

import com.shipsgame.domain.entity.Ships;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipsRepository extends JpaRepository<Ships, Long>, CrudRepository<Ships, Long> {

}
