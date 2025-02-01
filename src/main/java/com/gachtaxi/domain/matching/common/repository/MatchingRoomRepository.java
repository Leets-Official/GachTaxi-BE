package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomType;
import com.gachtaxi.domain.members.entity.Members;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {
    @Query("SELECT r FROM MatchingRoom r " +
        "WHERE " +
        "FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :startLatitude, :startLongitude), FUNCTION('POINT', r.route.startLatitude, r.route.startLongitude)) <= :radius " +
        "AND FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :destinationLatitude, :destinationLongitude), FUNCTION('POINT', r.route.endLatitude, r.route.endLongitude)) <= :radius ")
    List<MatchingRoom> findRoomsByStartAndDestination(
            @Param("startLongitude") double startLongitude,
            @Param("startLatitude") double startLatitude,
            @Param("destinationLongitude") double destinationLongitude,
            @Param("destinationLatitude") double destinationLatitude,
            @Param("radius") double radius
    );
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
            "FROM MatchingRoom r JOIN r.memberMatchingRoomChargingInfo m " +
            "WHERE m.members = :user "+
            "AND r.matchingRoomStatus = 'ACTIVE' "+
            "AND m.paymentStatus != 'LEFT'")
    boolean existsByMemberInMatchingRoom(@Param("user") Members user);

    Page<MatchingRoom> findByMatchingRoomTypeAndMatchingRoomStatus(MatchingRoomType type, MatchingRoomStatus status, Pageable pageable);

    @Query("SELECT m.matchingRoom FROM MemberMatchingRoomChargingInfo m WHERE m.members = :user ORDER BY m.matchingRoom.id DESC")
    Page<MatchingRoom> findByMemberInMatchingRoom(@Param("user") Members user, Pageable pageable);
}
