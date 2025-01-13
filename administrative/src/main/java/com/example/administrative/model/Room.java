package com.example.administrative.model;

import com.example.administrative.model.Enum.RoomType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;

    @Column(nullable = false, unique = true)
    private long roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    @Column(nullable = false)
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;
}
