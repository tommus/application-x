package com.todev.tvshows.program;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.todev.tvshows.program.Program.should;

@Configuration
class ProgramsStaticLoader {

  @Bean
  CommandLineRunner loadPrograms(final ProgramsRepository programsRepository) {
    return (e) -> {
      final LocalDate today = LocalDate.now();

      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 0))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 25))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 0))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 40))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 16, 10))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40))
          .get());
      programsRepository.save(should().prepareProgram()
          .startingAt(new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 23, 40))
          .get());
    };
  }
}
