package com.checkme.CheckMe.event.service;

import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    private EventService(EventRepository eventRepository){
        this.eventRepository = eventRepository;
    }
    public List<Event> getEvents() {
        return eventRepository.findAll();
    }

    public void addNewEvent(Event event) throws IllegalAccessException {
        Optional<Event> eventByEmail = eventRepository.findStudentById(event.getId());
        if (eventByEmail.isPresent()){
            throw new IllegalAccessException("id Taken");
        }
        eventRepository.save(event);
        System.out.println(event.getTitle());

    }
}
