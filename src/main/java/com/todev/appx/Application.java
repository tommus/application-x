package com.todev.appx;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.todev.appx.models.Program;
import com.todev.appx.models.Show;
import com.todev.appx.models.Station;
import com.todev.appx.repositories.ProgramsRepository;
import com.todev.appx.repositories.ShowsRepository;
import com.todev.appx.repositories.StationsRepository;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
public class Application {
  private static final Logger LOG = Logger.getLogger(Application.class);

  /**
   * Application entry point.
   *
   * @param args command-line arguments.
   */
  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  /**
   * This method populates service's repositories with testable data.
   *
   * @param stations an instance of {@link StationsRepository} repository.
   * @param shows an instance of {@link ShowsRepository} repository.
   * @param programs an instance of {@link ProgramsRepository} repository.
   * @return {@link CommandLineRunner} bean.
   */
  @Bean
  CommandLineRunner init(StationsRepository stations, ShowsRepository shows, ProgramsRepository programs) {
    return (evt) -> {
      final LocalDate today = LocalDate.now();

      final Station foo = new Station("Foo");
      final Station bar = new Station("Bar");
      final Station ban = new Station("Ban");
      stations.save(foo);
      stations.save(bar);
      stations.save(ban);
      LOG.info("Stations has been added.");

      final Show weather = new Show("Weather Forecast", "Everyday weather forecast.", 10);
      final Show melody = new Show("Melody Trivia", "Popular entertainment program.", 25);
      final Show friends = new Show("Friends", "Group of buddies goes through massive mayhem.", 25);
      final Show news = new Show("News", "Daily dose of information.", 20);
      final Show got = new Show("Game of Thrones", "Winter is coming.", 55);
      final Show taxi = new Show("Taxi", "A skilled pizza delivery boy tries to work off his driving record.", 86);
      final Show hag = new Show("Hansel and Gretel: Witch Hunt", "Bounty hunters tracks and kills witches.", 88);
      shows.save(weather);
      shows.save(melody);
      shows.save(friends);
      shows.save(news);
      shows.save(got);
      shows.save(taxi);
      shows.save(hag);
      LOG.info("Shows has been added.");

      programs.save(new Program(foo, weather,
          new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 0)));

      programs.save(new Program(foo, melody,
          new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 17, 25)));

      programs.save(new Program(bar, friends,
          new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 0)));

      programs.save(
          new Program(bar, news, new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 15, 40)));

      programs.save(
          new Program(bar, got, new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40)));

      programs.save(new Program(ban, weather,
          new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 16, 10)));

      programs.save(
          new Program(ban, taxi, new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 20, 40)));

      programs.save(
          new Program(ban, hag, new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth(), 23, 40)));

      LOG.info("Programs has been added.");
    };
  }

  /**
   * This method configures Swagger REST documentation plugin.
   *
   * @return Swagger configuration.
   */
  @Bean
  Docket docs() {
    return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select().paths(apiPaths()).build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("Application-X REST API")
        .description("Simple TV shows/stations REST service written in Java (Spring + Hibernate).")
        .contact(apiContact())
        .termsOfServiceUrl("https://www.gnu.org/licenses/gpl-3.0.html")
        .license("GNU GPL, Version 3")
        .licenseUrl("https://www.gnu.org/licenses/gpl-3.0.html")
        .version("0.1.1")
        .build();
  }

  /**
   * This method allows to configure endpoints that will be documented.
   *
   * @return a {@link Predicate} predicate that defines a list of documented endpoints.
   */
  private Predicate<String> apiPaths() {
    // Exclude Basic Error Controller.
    return Predicates.not(PathSelectors.regex("/error.*"));
  }

  /**
   * This method allows to configure contact information.
   *
   * @return a developer {@link Contact} contact information.
   */
  private Contact apiContact() {
    return new Contact("Tomasz Dzieniak", "https://github.com/tommus/tv-shows-rest-api", "tomasz.dzieniak@to-dev.com");
  }
}
