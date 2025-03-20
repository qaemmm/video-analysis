package com.videoanalysis.service;

import com.videoanalysis.model.User;
import com.videoanalysis.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.points.register}")
    private int registerPoints;

    @Value("${app.points.daily-login}")
    private int dailyLoginPoints;

    @Value("${app.points.parse-cost}")
    private int parseCost;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Transactional
    public User registerUser(String username, String password) {
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setPoints(registerPoints);
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Transactional
    public void handleDailyLogin(User user) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime lastLogin = user.getLastLoginDate();

        if (lastLogin == null || !lastLogin.toLocalDate().equals(now.toLocalDate())) {
            user.setPoints(user.getPoints() + dailyLoginPoints);
            user.setLastLoginDate(now);
            userRepository.save(user);
        }
    }

    @Transactional
    public boolean deductPoints(User user) {
        if (user.getPoints() < parseCost) {
            return false;
        }
        user.setPoints(user.getPoints() - parseCost);
        userRepository.save(user);
        return true;
    }

    @Transactional
    public void addPoints(User user, int points) {
        user.setPoints(user.getPoints() + points);
        userRepository.save(user);
    }
} 