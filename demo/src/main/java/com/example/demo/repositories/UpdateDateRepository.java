package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.UpdateDate;

public interface UpdateDateRepository extends JpaRepository<UpdateDate, UUID> {

}
