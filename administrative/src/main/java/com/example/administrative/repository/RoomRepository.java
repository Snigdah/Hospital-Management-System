package com.example.administrative.repository;

import com.example.administrative.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT MAX(r.roomNumber) FROM Room r")
    Optional<Long> findMaxRoomNumber();

    @Query("SELECT r FROM Room r ORDER BY r.roomNumber ASC")
    List<Room> findAllOrderByRoomNumberAsc();

    @Query("SELECT r FROM Room r WHERE r.department.deptName = :deptName ORDER BY r.roomNumber ASC")
    List<Room> findAllByDepartmentNameOrderByRoomNumberAsc(@Param("deptName") String deptName);

    Optional<Room> findByRoomId(long roomId);
}
