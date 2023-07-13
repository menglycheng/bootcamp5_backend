package com.checkme.CheckMe.event.entity;

import com.checkme.CheckMe.user.dto.UserProfileResponse;
import com.checkme.CheckMe.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name= "Event")
@Table
@NoArgsConstructor
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_sequence",
            sequenceName = "event_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_sequence"
    )

    private Long id;
    @Column(
            name="title",
            nullable = false,
            columnDefinition = "TEXT"
    )

    private String title;
    @Column(
            name="description",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;

    @Column(
            name="category",
            nullable = false
    )
    private Category category;
    @Column(
            name="location",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String location;
    @Column(
            name="poster",
            nullable = false
    )
    private String poster;
    @Column(
            name="register_url",
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String registerUrl;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private int views;
    @Column(
            name="deadline",
            nullable = false
    )
    private LocalDate deadline;

    @Builder
    public Event(Long id, String title, String description, Category category,
                 String location, String poster, String registerUrl, User user,
                 int views, LocalDate deadline) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.poster = poster;
        this.registerUrl = registerUrl;
        this.user = user;
        this.views = views;
        this.deadline = deadline;
    }

    // Constructors, getters, and setters

    // Define the Category enum
    public enum Category {
        COMPETITION,
        VOLUNTEER
    }

    // Getters and Setters

    public Long getId() {
        return id != null ? id : 0L;
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }



    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
