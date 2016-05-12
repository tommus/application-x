package com.todev.appx;

import com.todev.appx.models.Program;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramRepository;
import com.todev.appx.repositories.StationRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
@SpringBootApplication
public class Application {
    private static final Logger LOG = Logger.getLogger(Application.class);

    /**
     * Application entry point.
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * This method populates service's repositories with testable data.
     * @param programRepository an instance of {@link ProgramRepository} repository.
     * @param stationRepository an instance of {@link StationRepository} repository.
     * @return {@link CommandLineRunner} bean.
     */
    @Bean
    CommandLineRunner init(ProgramRepository programRepository, StationRepository stationRepository) {
        return (evt) -> {
            final LocalDate today = LocalDate.now();
            final Station tvp1 = new Station("TVP1");
            final Station tvp2 = new Station("TVP2");
            final Station polsat = new Station("Polsat");

            stationRepository.save(tvp1);
            stationRepository.save(tvp2);
            stationRepository.save(polsat);

            LOG.info("Stations has been added.");

            programRepository.save(new Program(
                tvp1,
                10,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 0),
                "Codzienny serwis informacyjny z prognozą pogody.",
                "Pogoda"
            ));

            programRepository.save(new Program(
                tvp1,
                25,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 25),
                "Popularny program rozrywkowy.",
                "Jaka to melodia?"
            ));

            programRepository.save(new Program(
                tvp2,
                40,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 0),
                "Polski serial komediowy w reżyserii Patricka Yoki, emitowany od 2 marca 2011 w TVP2",
                "Rodzinka.pl"
            ));

            programRepository.save(new Program(
                tvp2,
                20,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 40),
                "Program informacyjny - codzienny przegląd zdarzeń w kraju.",
                "Panorama - kraj"
            ));

            programRepository.save(new Program(
                tvp2,
                65,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40),
                "Adam organizuje dla Wiktorii imprezę urodzinową.",
                "Na dobre i na złe"
            ));

            programRepository.save(new Program(
                polsat,
                5,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 16, 10),
                "Codzienny serwis informacyjny z progrnozą pogody.",
                "Pogoda"
            ));

            programRepository.save(new Program(
                polsat,
                60,
                new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40),
                "Film o dwóch bohaterach Marsylii - komisarzu Emilienie i taksówkarzu Danielu.",
                "Taxi"
            ));

            LOG.info("Programs has been added.");
        };
    }
}
