package com.jay.h2.repository;

import com.jay.h2.model.Player;


import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.Query;

import com.jay.h2.model.Player;

import reactor.core.publisher.Flux;


public interface PlayerRepository extends ReactiveCrudRepository<Player, Long> {

    @Query("SELECT * FROM Player WHERE name = $1")
    Flux<Player> findByName(String name);

    @Query("SELECT * FROM Player WHERE age = $1")
    Flux<Player> findByAge(int age);
}