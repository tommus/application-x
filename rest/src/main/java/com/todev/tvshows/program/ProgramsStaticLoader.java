package com.todev.tvshows.program;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.todev.tvshows.program.Program.should;
import static java.time.LocalDateTime.of;

@Configuration
class ProgramsStaticLoader {

  @Bean
  CommandLineRunner loadPrograms(final ProgramsRepository programsRepository) {
    return (e) -> {
      final LocalDate today = LocalDate.now();

      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(17, 0))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(17, 25))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(15, 0))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(15, 40))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(20, 40))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(16, 10))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(20, 40))).get());
      programsRepository.save(should().prepareProgram().startingAt(of(today, LocalTime.of(23, 40))).get());
    };
  }
}
