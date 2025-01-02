package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.members.entity.Members;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Members, Long> {

}
