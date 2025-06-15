package com.sri.jwt.service;

import com.sri.jwt.configuration.util.JwtUtil;
import com.sri.jwt.dao.RefreshTokenRepository;
import com.sri.jwt.entity.JwtRequest;
import com.sri.jwt.entity.JwtResponse;
import com.sri.jwt.entity.RefreshToken;
import com.sri.jwt.user.dao.UserDao;
import com.sri.jwt.user.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

@Service
public class JwtService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private HttpServletRequest request;
/*
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticate(userName, password);

        UserDetails userDetails = loadUserByUsername(userName);
        String newGeneratedToken = jwtUtil.generateToken(userDetails); // valid for 2mins
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);  // valid for 7 days
        

        User user = userDao.findById(userName).get();
        return new JwtResponse(user, newGeneratedToken, refreshToken);
    }
    
    
    public JwtResponse refreshAccessToken(String refreshToken) throws Exception {
        String username = jwtUtil.getUsernameFromToken(refreshToken);

        UserDetails userDetails = loadUserByUsername(username);

        if (jwtUtil.validateToken(refreshToken, userDetails)) {
            String newAccessToken = jwtUtil.generateToken(userDetails);
            return new JwtResponse(userDao.findById(username).get(), newAccessToken, refreshToken);
        } else {
            throw new Exception("Invalid or expired refresh token");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();

        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user)
            );
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    
    */
    
    // this method provides access token 
    public JwtResponse createJwtToken(JwtRequest jwtRequest) throws Exception {
        String userName = jwtRequest.getUsername();
        String password = jwtRequest.getPassword();
        authenticate(userName, password);

        UserDetails userDetails = loadUserByUsername(userName);
        String newAccessToken = jwtUtil.generateToken(userDetails); // here we are calling Access Token

        String refreshTokenId = UUID.randomUUID().toString();  // here it generates Refresh Token ID when we call access Token and stored into DB
        

        RefreshToken tokenEntity = new RefreshToken();
        tokenEntity.setId(refreshTokenId);
        tokenEntity.setUsername(userName);
        tokenEntity.setExpiresAt(Instant.now().plus(2, ChronoUnit.MINUTES));
        tokenEntity.setIpAddress(request.getRemoteAddr());
        tokenEntity.setUserAgent(request.getHeader("User-Agent"));
        refreshTokenRepository.save(tokenEntity);

        User user = userDao.findById(userName).get();
        return new JwtResponse(user, newAccessToken, refreshTokenId.toString());
    }

    // Rotating Refresh Token Logic
    public JwtResponse refreshAccessToken(String refreshTokenId) throws Exception {
        String tokenUUID = UUID.fromString(refreshTokenId).toString();
        RefreshToken tokenEntity = refreshTokenRepository.findById(tokenUUID)
            .orElseThrow(() -> new Exception("Invalid refresh token"));

        if (tokenEntity.getExpiresAt().isBefore(Instant.now()) || Boolean.TRUE.equals(tokenEntity.getRevoked())) {
            throw new Exception("Refresh token expired or reused");
        }

        tokenEntity.setRevoked(true);
        refreshTokenRepository.save(tokenEntity);

        UserDetails userDetails = loadUserByUsername(tokenEntity.getUsername());
        String newAccessToken = jwtUtil.generateToken(userDetails);

        String newRefreshTokenId = UUID.randomUUID().toString();
        RefreshToken newToken = new RefreshToken();
        newToken.setId(newRefreshTokenId);
        newToken.setUsername(tokenEntity.getUsername());
        newToken.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS));
        newToken.setIpAddress(request.getRemoteAddr());
        newToken.setUserAgent(request.getHeader("User-Agent"));
        refreshTokenRepository.save(newToken);

        return new JwtResponse(userDao.findById(tokenEntity.getUsername()).get(), newAccessToken, newRefreshTokenId.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findById(username).get();
        if (user != null) {
            return new org.springframework.security.core.userdetails.User(
                    user.getUserName(),
                    user.getUserPassword(),
                    getAuthority(user));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        });
        return authorities;
    }

    private void authenticate(String userName, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
    
    public String findByIpAddressAndUserAgent(RefreshToken refreshTokenId) throws Exception {
    
    	String ipAddress = refreshTokenId.getIpAddress();
    	String userAgent = refreshTokenId.getUserAgent();
    	
    	String getRefreshTokenID = refreshTokenRepository.findByIpAddressAndUserAgent(ipAddress, userAgent);
    	return getRefreshTokenID;
    }
}



