package com.checkme.CheckMe.event.entity;
import com.checkme.CheckMe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.sql.ConnectionBuilder;
import java.time.LocalDateTime;

@Entity(name= "favorite_event")
@Table
@NoArgsConstructor
public class FavoriteEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private LocalDateTime createdAt;

    @Builder
    public FavoriteEvent(Long id, User user, Event event, LocalDateTime createdAt) {
        this.id = id;
        this.user = user;
        this.event = event;
        this.createdAt = createdAt;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
