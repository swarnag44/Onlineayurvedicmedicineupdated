package com.cg.oam.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.oam.entity.Medicine;
public interface MedicineRepository extends JpaRepository<Medicine, String> {

}

