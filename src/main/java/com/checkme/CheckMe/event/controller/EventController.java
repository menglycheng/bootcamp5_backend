package com.checkme.CheckMe.event.controller;
import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.repository.EventRepository;
import com.checkme.CheckMe.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;


    @Autowired
    private EventController(EventRepository eventRepository, EventService eventService){

        this.eventService = eventService;
    }

    @GetMapping
    public List<Event> getEvents(@RequestParam("status") String status, @RequestParam("category") String category) {
        return eventService.getEvents(status, category);
    }

    @PostMapping("/post")
    public void PostEvent(@RequestBody  Event event) throws IllegalAccessException {
        eventService.addNewEvent(event);
    }
    @DeleteMapping("/delete")
    public void deleteEvent(@RequestParam("id") Long id) {
        eventService.deleteEvent(id);
    }
}