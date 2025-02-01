package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.members.entity.Blacklists;
import com.gachtaxi.domain.members.entity.Members;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistsRepository extends JpaRepository<Blacklists, Long> {

  Optional<Blacklists> findByRequesterAndReceiver(Members requester, Members receiver);
  boolean existsByRequesterAndReceiver(Members requester, Members receiver);
}
