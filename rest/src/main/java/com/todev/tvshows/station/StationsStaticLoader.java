package com.todev.tvshows.station;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.todev.tvshows.station.Station.should;

@Configuration
class StationsStaticLoader {

  @Bean
  CommandLineRunner loadStations(final StationsRepository stationsRepository) {
    return (e) -> {
      stationsRepository.save(should().prepareStation().named("Foo").get());
      stationsRepository.save(should().prepareStation().named("Bar").get());
      stationsRepository.save(should().prepareStation().named("Baz").get());
    };
  }
}
