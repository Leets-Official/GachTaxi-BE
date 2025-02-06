package com.gachtaxi.domain.matching.common.repository;

import com.gachtaxi.domain.matching.common.entity.MatchingRoom;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomStatus;
import com.gachtaxi.domain.matching.common.entity.enums.MatchingRoomType;
import com.gachtaxi.domain.members.entity.Members;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchingRoomRepository extends JpaRepository<MatchingRoom, Long> {
//    @Query("SELECT r FROM MatchingRoom r " +
//        "WHERE " +
//        "FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :startLongitude, :startLatitude), FUNCTION('POINT', r.route.startLongitude, r.route.startLatitude)) <= :radius " +
//        "AND FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :destinationLongitude, :destinationLatitude), FUNCTION('POINT', r.route.endLongitude, r.route.endLatitude)) <= :radius ")
//    List<MatchingRoom> findRoomsByStartAndDestination(
//            @Param("startLongitude") double startLongitude,
//            @Param("startLatitude") double startLatitude,
//            @Param("destinationLongitude") double destinationLongitude,
//            @Param("destinationLatitude") double destinationLatitude,
//            @Param("radius") double radius
//    );
    /**
     * 출발지와 도착지 기준으로 매칭 방 찾기 (위도, 경도 제거)
     */
    @Query("SELECT r FROM MatchingRoom r " +
            "WHERE r.departure = :departure " +
            "AND r.destination = :destination " +
            "AND r.matchingRoomStatus = 'ACTIVE'" +
            "AND r.matchingRoomType != 'MANUAL' ")
    List<MatchingRoom> findRoomsByDepartureAndDestination(@Param("departure") String departure, @Param("destination") String destination);

    @Query("SELECT r " +
            "FROM MatchingRoom r JOIN r.memberMatchingRoomChargingInfo m " +
            "WHERE m.members = :user "+
            "AND r.matchingRoomStatus = 'ACTIVE' "+
            "AND m.paymentStatus != 'LEFT'")
    List<MatchingRoom> findByMemberInMatchingRoom(@Param("user") Members user);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
        "FROM MatchingRoom r JOIN r.memberMatchingRoomChargingInfo m " +
        "WHERE m.members = :user "+
        "AND r.matchingRoomStatus = 'ACTIVE' "+
        "AND m.paymentStatus != 'LEFT'")
    boolean existsByMemberInMatchingRoom(@Param("user") Members user);

    Page<MatchingRoom> findByMatchingRoomTypeAndMatchingRoomStatus(MatchingRoomType type, MatchingRoomStatus status, Pageable pageable);

    @Query("SELECT m.matchingRoom FROM MemberMatchingRoomChargingInfo m " +
            "WHERE m.members = :user " +
            "AND m.matchingRoom.matchingRoomStatus = 'ACTIVE' " +
            "AND m.paymentStatus != 'LEFT'" +
            "ORDER BY m.matchingRoom.id DESC")
    Page<MatchingRoom> findByMemberInMatchingRoom(@Param("user") Members user, Pageable pageable);
}
