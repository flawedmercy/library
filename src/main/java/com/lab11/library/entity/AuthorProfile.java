package com.lab11.library.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "author_profiles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AuthorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String bio;

    @Column(length = 100)
    private String nationality;

    private Integer birthYear;

    // One-to-One owning side
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;
}