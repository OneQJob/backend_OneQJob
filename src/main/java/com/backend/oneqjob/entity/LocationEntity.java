package com.backend.oneqjob.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "locations")
public class LocationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "road_address")
    private String roadAddress;

    private String sido;

    private String sigungu;

    private String bname;

    @Column(nullable = true)
    private String bname1;

    @Column(name = "detail_address", nullable = true)
    private String detailAddress;

    @Column(nullable = true)
    private BigDecimal latitude;

    @Column(nullable = true)
    private BigDecimal longitude;
}
