package com.checkme.CheckMe.event.repository;

import com.checkme.CheckMe.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findEventById(Long id);
}
