package com.checkme.CheckMe.event.controller;

import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.repository.EventRepository;
import com.checkme.CheckMe.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping("/update/{id}")
    public void updateEvent(@PathVariable("id") Long id, @RequestBody Event event) {
        eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@RequestParam("id") Long id) {
        eventService.deleteEvent(id);
    }
}