package com.todev.tvshows.show;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.todev.tvshows.show.Show.should;

@Configuration
class ShowsStaticLoader {

  @Bean
  CommandLineRunner loadShows(final ShowsRepository showsRepository) {
    return (e) -> {
      showsRepository.save(should().prepareShow()
          .named("Weather Forecast")
          .briefed("Everyday weather forecast")
          .lasting(10)
          .get());
      showsRepository.save(should().prepareShow()
          .named("Melody Trivia")
          .briefed("Popular entertainment program.")
          .lasting(25)
          .get());
      showsRepository.save(should().prepareShow()
          .named("Friends")
          .briefed("Group of buddies goes through massive mayhem.")
          .lasting(25)
          .get());
      showsRepository.save(should().prepareShow()
          .named("News")
          .briefed("Daily dose of information.")
          .lasting(20)
          .get());
      showsRepository.save(should().prepareShow()
          .named("Game of Thrones")
          .briefed("Winter is coming.")
          .lasting(55)
          .get());
      showsRepository.save(should().prepareShow()
          .named("Taxi")
          .briefed("A skilled pizza delivery boy tries to work off his driving record.")
          .lasting(86)
          .get());
      showsRepository.save(should().prepareShow()
          .named("Hansel and Gretel: Witch Hunt")
          .briefed("Bounty hunters tracks and kills witches.")
          .lasting(88)
          .get());
    };
  }
}
