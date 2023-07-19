package com.checkme.CheckMe.event.dto;

import com.checkme.CheckMe.event.entity.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;
    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;
    @NotNull(message = "Category cannot be null")
    @NotBlank(message = "Category cannot be blank")
    private Event.Category category;
    @NotNull(message = "Location cannot be null")
    @NotBlank(message = "Location cannot be blank")
    private String location;
    @NotNull(message = "Poster cannot be null")
    private MultipartFile poster;
    @NotNull(message = "Register URL cannot be null")
    @NotBlank(message = "Register URL cannot be blank")
    private String registerUrl;
    @NotNull(message = "Deadline cannot be null")
    private LocalDate deadline;
}
