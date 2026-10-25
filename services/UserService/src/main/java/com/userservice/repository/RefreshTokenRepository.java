package com.userservice.repository;


import com.userservice.model.RefreshToken;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("""
            update RefreshToken r
            set r.revoked = true
            where r.familyId = :familyId
        """)
    int revokeFamily(@Param("familyId") String familyId);

    @Modifying
    @Query("""
            update RefreshToken r
            set r.revoked = true
            where r.user.id = :userId
        """)
    int revokeAllByUserId(@Param("userId") Long userId);

}
