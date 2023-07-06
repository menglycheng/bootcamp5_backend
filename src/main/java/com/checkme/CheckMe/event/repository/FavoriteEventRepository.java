package com.checkme.CheckMe.event.repository;
import com.checkme.CheckMe.event.entity.FavoriteEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FavoriteEventRepository extends JpaRepository<FavoriteEvent, Long> {
}
