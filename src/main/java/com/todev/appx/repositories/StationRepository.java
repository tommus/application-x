package com.todev.appx.repositories;

import com.todev.appx.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Tomasz Dzieniak on 13.05.16.
 */
public interface StationRepository extends JpaRepository<Station, Long> {
    /**
     * Query method that returns all stored {@link Station}s.
     * @return a collection of {@link Station} items.
     */
    List<Station> findAll();

    /**
     * Query method that returns an instance of {@link Station} object at given ID.
     * @param id an ID of item which should be retrieved.
     * @return an instance of a {@link Station} object.
     */
    Station findById(long id);
}
