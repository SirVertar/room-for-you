package com.mateusz.jakuszko.roomforyou.repository;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeletedCustomerRepository extends JpaRepository<DeletedCustomer, Long> {
}
