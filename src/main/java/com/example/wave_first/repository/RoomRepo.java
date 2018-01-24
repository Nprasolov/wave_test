package com.example.wave_first.repository;

import com.example.wave_first.entity.Room;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends CrudRepository<Room,Long>{
    Room findRoomByName(long room_id);

}
