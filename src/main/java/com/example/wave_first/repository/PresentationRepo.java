package com.example.wave_first.repository;


import com.example.wave_first.entity.Presentation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PresentationRepo extends CrudRepository<Presentation, Long> {

}
