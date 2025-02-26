package com.example.demo.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Friend;

@Repository
public interface FriendRepository extends JpaRepository<Friend, UUID> {

}
