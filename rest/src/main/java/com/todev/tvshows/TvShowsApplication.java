package com.todev.tvshows;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@ComponentScan(basePackages = "com.todev.tvshows")
public class TvShowsApplication extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(TvShowsApplication.class, args);
  }

  @Override
  protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
    return builder.sources(TvShowsApplication.class);
  }

  @RestController
  class HelloController {

    @RequestMapping("hello")
    String hello() {
      return "Hello there!";
    }
  }
}
