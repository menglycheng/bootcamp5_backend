package com.checkme.CheckMe.event.controller;

import com.checkme.CheckMe.event.dto.EventRequest;
import com.checkme.CheckMe.event.dto.UpdateEventRequest;
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

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable("id") Long id) {
        return eventService.findEventById(id);
    }

    @GetMapping("/user")
    public List<Event> getEventsByUsername(@RequestParam("username") String username) {
        return eventService.getEventsByUsername(username);
    }
    @GetMapping
    public List<Event> getEvents(@RequestParam("status") String status, @RequestParam("category") String category) {
        return eventService.getEvents(status, category);
    }

    @PostMapping(value = "/post", consumes = "multipart/form-data")
    public void PostEvent(@RequestBody @ModelAttribute EventRequest event) throws IllegalAccessException {
        eventService.addNewEvent(event);
    }

    @PostMapping("/view")
    public void incrementViewCount(@RequestParam("id") Long id) {
        eventService.incrementViewCount(id);
    }

    @PutMapping(value = "/update", consumes = "multipart/form-data")
    public void updateEvent(@RequestParam("id") Long id, @RequestBody @ModelAttribute UpdateEventRequest event) {
        eventService.updateEvent(id, event);
    }

    @DeleteMapping("/delete")
    public void deleteEvent(@RequestParam("id") Long id) {
        eventService.deleteEvent(id);
    }
}