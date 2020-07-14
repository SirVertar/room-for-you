package com.mateusz.jakuszko.roomforyou.repository;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedApartment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedApartmentRepository extends JpaRepository<DeletedApartment, Long> {
}
