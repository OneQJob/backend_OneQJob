package com.backend.oneqjob.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="user_name",nullable = false)
    private String userName;
    @Column(name="phone_number",nullable = false)
    private String phoneNumber;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private LocationEntity address;
    @Column(name="img_file_name",nullable = true)
    private String imgFileName;
    @Column(name="img_url",nullable = true)
    private String imgUrl;
    @Column(name="date_of_birth")
    private String dateOfBirth;
    @Column(name="user_id",nullable = false)
    private String userId;
    @Column(name="user_pw",nullable = false)
    private String userPw;
}
