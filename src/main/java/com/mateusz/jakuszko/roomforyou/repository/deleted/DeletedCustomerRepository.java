package com.mateusz.jakuszko.roomforyou.repository.deleted;

import com.mateusz.jakuszko.roomforyou.entity.deleted.DeletedCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface DeletedCustomerRepository extends JpaRepository<DeletedCustomer, Long> {
}
