package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.RecurringInvoices;

@Repository
public interface RecurringInvoicesRepository extends JpaRepository<RecurringInvoices, UUID> {

}
