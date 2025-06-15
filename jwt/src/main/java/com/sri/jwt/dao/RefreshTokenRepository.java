package com.sri.jwt.dao;

import com.sri.jwt.entity.RefreshToken;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, String> {
    List<RefreshToken> findByUsernameAndRevokedFalse(String username);
    
    
    @Query("SELECT c.id FROM RefreshToken c WHERE c.ipAddress = :ipAddress and c.userAgent = :userAgent")
    String findByIpAddressAndUserAgent(String ipAddress, String userAgent);
    
}
