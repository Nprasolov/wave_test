package com.example.wave_first.repository;

import com.example.wave_first.entity.UserPresentation;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsPrRepo extends CrudRepository<UserPresentation,Long> {
    Iterable<UserPresentation> findUserPresentationByUserId(long user_id);
    Iterable<UserPresentation> findUserPresentationByPresentationId(long presentation_id);
}
