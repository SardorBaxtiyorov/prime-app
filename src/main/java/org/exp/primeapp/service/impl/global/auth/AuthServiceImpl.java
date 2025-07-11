package org.exp.primeapp.service.impl.global.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.exp.primeapp.configs.security.JwtService;
import org.exp.primeapp.models.dto.request.LoginReq;
import org.exp.primeapp.models.dto.request.RegisterReq;
import org.exp.primeapp.models.dto.request.VerifyEmailReq;
import org.exp.primeapp.models.dto.responce.global.ApiResponse;
import org.exp.primeapp.models.dto.responce.global.LoginRes;
import org.exp.primeapp.models.entities.Role;
import org.exp.primeapp.models.entities.User;
import org.exp.primeapp.repository.RoleRepository;
import org.exp.primeapp.repository.UserRepository;
import org.exp.primeapp.service.interfaces.global.auth.AuthService;
import org.exp.primeapp.service.interfaces.global.auth.EmailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;

    @Override
    public LoginRes login(LoginReq loginReq) {
        try {
            var auth = new UsernamePasswordAuthenticationToken(
                    loginReq.getEmail(),
                    loginReq.getPassword()
            );

            // Bu yerda: email/password + isEnabled() avtomatik tekshiriladi
            authenticationManager.authenticate(auth);

            // load user to generate token
            User user = (User) userDetailsService.loadUserByUsername(loginReq.getEmail());

            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);

            return new LoginRes(accessToken, refreshToken, "success");

        } catch (DisabledException e) {
            return new LoginRes(null, null, "Account not active");
        } catch (BadCredentialsException e) {
            return new LoginRes(null, null, "Invalid email or password");
        } catch (UsernameNotFoundException e) {
            return new LoginRes(null, null, "User not found");
        } catch (Exception e) {
            return new LoginRes(null, null, "Login failed");
        }
    }

    @Override
    @Transactional
    public ApiResponse sendVerificationCode(RegisterReq req) {
        if (!req.getPassword().equals(req.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match.");
        }

        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already exists.");
        }

        String code = String.valueOf(1000 + new Random().nextInt(9000));

        List<Role> roleUser = roleRepository.findALlByNameIn(List.of("ROLE_USER"));

        User user = User.builder()
                .firstName(req.getFirstName())
                .lastName(req.getLastName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .phone(req.getPhone())
                .active(false)
                .roles(roleUser)
                .verifyCode(code)
                .build();
        userRepository.save(user);

        emailService.sendVerificationEmail(req.getEmail(), code);
        return new ApiResponse(true, "Verification code sent to email");
    }

    @Override
    public ApiResponse verifyCodeAndRegister(VerifyEmailReq req) {
        Optional<User> optionalUser = userRepository.getByEmailOptional(req.getEmail());

        if (optionalUser.isEmpty()) {
            return new ApiResponse(false, "Invalid email address.");
        }

        User user = optionalUser.get();
        String verifyCode = user.getVerifyCode();

        if (verifyCode != null && verifyCode.equals(req.getCode())) {
            user.setActive(true);
            userRepository.save(user);
            return new ApiResponse(true, "User registered successfully");
        } else {
            return new ApiResponse(false, "Invalid verification code.");
        }
    }
}
