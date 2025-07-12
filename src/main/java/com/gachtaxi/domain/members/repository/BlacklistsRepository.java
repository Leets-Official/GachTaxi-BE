package com.gachtaxi.domain.members.repository;

import com.gachtaxi.domain.members.entity.Blacklists;
import com.gachtaxi.domain.members.entity.Members;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BlacklistsRepository extends JpaRepository<Blacklists, Long> {

  Optional<Blacklists> findByRequesterAndReceiver(Members requester, Members receiver);
  boolean existsByRequesterAndReceiver(Members requester, Members receiver);
  Slice<Blacklists> findAllByRequester(Members requester, Pageable pageable);

  @Query("""
      SELECT b
      FROM Blacklists b
      JOIN FETCH b.receiver r
      WHERE b.requester = :requester
        AND LOWER(r.nickname) LIKE LOWER(CONCAT('%', :keyword, '%'))
      ORDER BY r.nickname ASC
  """)
  Slice<Blacklists> searchMyBlackLists(
          @Param("requester") Members requester,
          @Param("keyword")     String keyword,
          Pageable pageable
  );
}
