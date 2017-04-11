package com.todev.tvshows.show;

import com.todev.tvshows.show.Show.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ShowsStaticLoader {

  @Bean
  CommandLineRunner loadShows(final ShowsRepository showsRepository) {
    return (e) -> {
      showsRepository.save(new Builder()
          .named("Weather Forecast")
          .briefed("Everyday weather forecast.")
          .lasting(10)
          .get());
      showsRepository.save(new Builder()
          .named("Melody Trivia")
          .briefed("Popular entertainment program.")
          .lasting(25)
          .get());
      showsRepository.save(new Builder()
          .named("Friends")
          .briefed("Group of buddies goes through massive mayhem.")
          .lasting(25)
          .get());
      showsRepository.save(new Builder()
          .named("News")
          .briefed("Daily dose of information.")
          .lasting(20)
          .get());
      showsRepository.save(new Builder()
          .named("Game of Thrones")
          .briefed("Winter is coming.")
          .lasting(55)
          .get());
      showsRepository.save(new Builder()
          .named("Taxi")
          .briefed("A skilled pizza delivery boy tries to work off his driving record.")
          .lasting(86)
          .get());
      showsRepository.save(new Builder().named("Hansel and Gretel: Witch Hunt")
          .briefed("Bounty hunters tracks and kills witches.")
          .lasting(88)
          .get());
    };
  }
}
