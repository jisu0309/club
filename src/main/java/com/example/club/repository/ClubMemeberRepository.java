package com.example.club.repository;

import com.example.club.entity.ClubMember;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ClubMemeberRepository extends JpaRepository<ClubMember, String> {

    // 사용자 이메일과 소셜로그인 false여부로 검색
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD) //left outer join으로 role처리
    @Query("select m from ClubMember m where m.fromSocial = :social and m.email = :email")
    Optional<ClubMember> findByEmail(String email, boolean social);

}
