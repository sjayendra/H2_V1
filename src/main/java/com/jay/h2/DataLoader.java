package com.jay.h2;

import com.jay.h2.model.FbPlayer;
import com.jay.h2.repository.FbPlayerRepository;
import com.jay.h2.repository.PlayerRepository;
import io.r2dbc.h2.H2ConnectionFactory;

import  org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.r2dbc.core.DatabaseClient;
import javax.annotation.PostConstruct;
import org.springframework.core.io.Resource;

import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import java.util.List;
import com.jay.h2.model.Player;
import com.jay.h2.model.FbPlayer;

import java.util.Arrays;

import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;

import reactor.test.StepVerifier;

import reactor.core.publisher.Flux;

@Component
public class DataLoader {

    private static final Logger LOGGER=LoggerFactory.getLogger(DataLoader.class);


    private  String db_sql = "CREATE table player (id INT AUTO_INCREMENT NOT NULL, name VARCHAR2, age INT NOT NULL);";
    private String  fb_sql = "CREATE table fb_player (id INT AUTO_INCREMENT NOT NULL, name VARCHAR2, age INT NOT NULL);";
    //@Autowired
    //FbPlayerRepository PlayerRepository;

    Resource resource = new ByteArrayResource(db_sql.getBytes());
    Resource resource_fb = new ByteArrayResource(fb_sql.getBytes());

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    FbPlayerRepository fbPlayerRepository;

    //@Autowired
    //DatabaseClient databaseClient;

    @Autowired
    H2ConnectionFactory factory;


    public DataLoader(H2ConnectionFactory factory) {
        this.factory = factory;
        LOGGER.info("DataLoader Constructor");
    }

    @PostConstruct
    public void loadData(){



            LOGGER.info("DataLoader Start");
            System.out.println("start");


            ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
            initializer.setConnectionFactory(factory);
            // This will create our database table and schema
            initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));

            initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource_fb));

        R2dbcEntityTemplate template = new R2dbcEntityTemplate(factory);

        R2dbcEntityTemplate template1 = new R2dbcEntityTemplate(factory);

        template.getDatabaseClient().sql(db_sql)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();

        template1.getDatabaseClient().sql(fb_sql)
                .fetch()
                .rowsUpdated()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .verifyComplete();



        //initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));

        //initializer.setDatabasePopulator(new ResourceDatabasePopulator(resource));

        LOGGER.info("DataLoader Start Inserting Players");


            List<Player> players = Arrays.asList(
                new Player(null, "Kaka", 37),
                new Player(null, "Messi", 32),
                new Player(null, "Mbappé", 20),
                new Player(null, "CR7", 34),
                new Player(null, "Lewandowski", 30),
                new Player(null, "Cavani", 32)
            );

            playerRepository.saveAll(players).subscribe();

            LOGGER.info("DataLoader : Completed Inserting Players");

            //Selecting all players from Table player

            LOGGER.info("Flux started");

            Flux<Player> player1 = template.select(Player.class).all()
                    .map(map-> {
                        System.out.println(map.getId());
                        System.out.println(map.getName());
                        return null;
                    });

            Flux<Player> player2 = template.select(Player.class).all();

            fbPlayerRepository.saveAll(player2.map (name-> new FbPlayer(null, name.getName(), name.getAge()))).subscribe();


           // player2
           //         .map(name-> new FbPlayer(null, name.getName(), name.getAge())
           // );
            //fbPlayerRepository.saveAll(FbPlayer);


            player2.subscribe(
                    f-> System.out.println("FLUXM " + f.getName())

            );



            player1.map(map->{
                        /* Table
                         *   id | name | age
                         */
                        System.out.println("id: " + map.getId());
                        System.out.println("name: "+ map.getName());
                        return player1;
                    });

        this.playerRepository
                .findAll()
                .subscribe((player)->LOGGER.info(player.getId()+" "+player.getName()+" "+player.getAge()) );




        LOGGER.info("Flux ended");

        Flux<FbPlayer> player4 = template1.select(FbPlayer.class).all();

        player4.subscribe(
                f4-> System.out.println("Flux FbPlayer " + f4.getId() + f4.getName())
        );


    }


}
