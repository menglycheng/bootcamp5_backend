package com.checkme.CheckMe.event.controller;
import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.repository.EventRepository;
import com.checkme.CheckMe.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event")
public class EventController {
    private final EventService eventService;

    @Autowired
    private EventController(EventRepository eventRepository, EventService eventService){

        this.eventService = eventService;
    }

    @GetMapping("/event-list")
    public List<Event> getEvents() {
        return eventService.getEvents();
    }
    @PostMapping("/post")
    public void PostEvent(@RequestBody  Event event) throws IllegalAccessException {
        eventService.addNewEvent(event);
    }

}