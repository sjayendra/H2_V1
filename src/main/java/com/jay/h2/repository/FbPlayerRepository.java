package com.jay.h2.repository;

import com.jay.h2.model.FbPlayer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.r2dbc.repository.Query;

import com.jay.h2.model.FbPlayer;

import reactor.core.publisher.Flux;

public interface FbPlayerRepository extends  ReactiveCrudRepository<FbPlayer, Long>{
    @Query("SELECT * FROM fbplayer WHERE name = $1")
    Flux<FbPlayer> findByName(String name);

    @Query("SELECT * FROM fbplayer WHERE age = $1")
    Flux<FbPlayer> findByAge(int age);

}
