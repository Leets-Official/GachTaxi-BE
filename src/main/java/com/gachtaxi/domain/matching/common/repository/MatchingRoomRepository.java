package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.Tags;
import com.gachtaxi.domain.members.entity.Members;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {
    @Query("SELECT r FROM MatchingRoom r " +
            "JOIN r.matchingRoomTagInfo ti " +
            "WHERE " +
            "FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :startLongitude, :startLatitude), FUNCTION('POINT', r.route.startLongitude, r.route.startLatitude)) <= 300 " +
            "AND FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :destinationLongitude, :destinationLatitude), FUNCTION('POINT', r.route.endLongitude, r.route.endLatitude)) <= 300 " +
            "AND ti.tags IN (:tags) " +
            "AND r.matchingRoomStatus = 'ACTIVE'")
    List<MatchingRoom> findRoomsByStartAndDestinationAndTags(
            @Param("startLongitude") double startLongitude,
            @Param("startLatitude") double startLatitude,
            @Param("destinationLongitude") double destinationLongitude,
            @Param("destinationLatitude") double destinationLatitude,
            @Param("tags") List<Tags> tags
    );

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            "FROM MatchingRoom r JOIN r.memberMatchingRoomChargingInfo m " +
            "WHERE m.members = :user")
    boolean existsByMemberInMatchingRoom(@Param("user") Members user);
}
