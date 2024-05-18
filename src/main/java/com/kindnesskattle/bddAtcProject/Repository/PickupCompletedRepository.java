package com.kindnesskattle.bddAtcProject.Repository;



import com.kindnesskattle.bddAtcProject.Entities.PickupCompleted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PickupCompletedRepository extends JpaRepository<PickupCompleted, Long> {
    List<PickupCompleted> findByPickedUpByUserId(Long userId);
}
