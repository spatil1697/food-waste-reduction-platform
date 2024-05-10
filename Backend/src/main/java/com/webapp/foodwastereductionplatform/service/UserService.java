package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.Configuration.*;
import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.model.User;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserServiceImplementation customUserDetails;


    public AuthUserRequestDTO createUser(UserSignUpRequestDTO userSignUpRequest) {

        if (userSignUpRequest.getEmail() == null || userSignUpRequest.getPassword() == null) {
            throw new IllegalArgumentException("Missing input data");
        }

        String userType = userSignUpRequest.getUserType().toLowerCase();
        if (!userType.equals("individual") && !userType.equals("organization")) {
            throw new IllegalArgumentException("User type must be Individual or Organization");
        }

        if (!isValidEmail(userSignUpRequest.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!isValidPassword(userSignUpRequest.getPassword())) {
            throw new IllegalArgumentException("Password must have at least one number, one capital letter, and one special character");
        }

        String email = userSignUpRequest.getEmail();
        String password = userSignUpRequest.getPassword();
        String firstName = userSignUpRequest.getFirstName();
        String lastName = userSignUpRequest.getLastName();
        String contactNumber = userSignUpRequest.getContactNumber();
        Address address = userSignUpRequest.getAddress();
        User isEmailExist = userRepository.findByEmail(email);

        if (isEmailExist != null) {
            throw new IllegalArgumentException("Email is already used with another account. Please use a different email address.");
        }

        User createdUser = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .address(address)
                .contactNumber(contactNumber)
                .userType(userType)
                .build();
        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);
        return AuthUserRequestDTO.builder()
                .message("SignUp success")
                .jwt(token)
                .status(true)
                .build();
    }

    public AuthUserResponseDTO login(UserLoginRequestDTO loginRequest) {

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(email, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = JwtProvider.generateToken(authentication);
        User user = userRepository.findByEmail(email);

        return AuthUserResponseDTO.builder()
                .message("Login success")
                .jwt(token)
                .status(true)
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .address(user.getAddress())
                .contactNumber(user.getContactNumber())
                .userType(user.getUserType())
                .email(user.getEmail())
                .build();
    }

    public AuthUserResponseDTO updateUserData(UserSignUpUpdateRequestDTO userSignUpRequestDTO, Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with ID " + userId + " not found"));

        String userType = userSignUpRequestDTO.getUserType().toLowerCase();
        if (!userType.equals("individual") && !userType.equals("organization")) {
            throw new IllegalArgumentException("User type must be Individual or Organization");
        }

        user.setFirstName(userSignUpRequestDTO.getFirstName());
        user.setLastName(userSignUpRequestDTO.getLastName());
        user.setAddress(userSignUpRequestDTO.getAddress());
        user.setUserType(userSignUpRequestDTO.getUserType());
        user.setContactNumber(userSignUpRequestDTO.getContactNumber());
        User updatedUser = userRepository.save(user);

        return AuthUserResponseDTO.builder()
                .message("User updated successfully")
                .status(true)
                .firstName(updatedUser.getFirstName())
                .lastName(updatedUser.getLastName())
                .address(updatedUser.getAddress())
                .contactNumber(updatedUser.getContactNumber())
                .userType(updatedUser.getUserType())
                .email(updatedUser.getEmail())
                .build();
    }

    public void deleteUserById(Integer userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
        userRepository.delete(user);
    }

    private Authentication authenticate(String email, String password) {

        UserDetails userDetails = customUserDetails.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid email and password");
        }
        if (!this.passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[!@#$%^&*()-_=+\\\\|[{]};:'\",<.>/?]).{8,}$";
        return password.matches(passwordRegex);
    }
}
