package com.semoi.semo.position.repository;

import com.semoi.semo.position.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

// 주현우, 차현철
@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {

    Optional<Position> findByName(String name);
}