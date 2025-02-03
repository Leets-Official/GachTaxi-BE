package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
}
