package com.todev.tvshows.station;

import com.todev.tvshows.station.Station.Builder;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class StationsStaticLoader {

  @Bean
  CommandLineRunner loadStations(final StationsRepository stationsRepository) {
    return (e) -> {
      stationsRepository.save(new Builder().named("Foo").get());
      stationsRepository.save(new Builder().named("Bar").get());
      stationsRepository.save(new Builder().named("Baz").get());
    };
  }
}
