package com.example.wave_first.repository;

import com.example.wave_first.entity.Schedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepo extends CrudRepository<Schedule,Long>{
    Iterable<Schedule> findScheduleByRoomId(long room_id);
}
