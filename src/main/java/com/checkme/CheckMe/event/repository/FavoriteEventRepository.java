package com.checkme.CheckMe.event.repository;
import com.checkme.CheckMe.event.entity.Event;
import com.checkme.CheckMe.event.entity.FavoriteEvent;
import com.checkme.CheckMe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {

    FavoriteEvent findByUserAndEvent(User currentUser, Event event);
}
