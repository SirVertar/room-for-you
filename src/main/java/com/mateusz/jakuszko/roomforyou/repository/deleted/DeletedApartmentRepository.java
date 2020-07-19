package com.mateusz.jakuszko.roomforyou.repository.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DeletedApartmentRepository extends JpaRepository<DeletedApartment, Long> {
}
