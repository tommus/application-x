package com.todev.appx.repositories;

import com.todev.appx.models.Program;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public interface ProgramRepository extends JpaRepository<Program, Long> {
    /**
     * Query method that returns all stored {@link Program}s.
     * @return a collection of {@link Program} items.
     */
    List<Program> findAll();
}
