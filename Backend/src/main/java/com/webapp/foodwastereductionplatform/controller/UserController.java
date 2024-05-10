package com.webapp.foodwastereductionplatform.controller;


import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/user")
@Tag(name = "User")
@AllArgsConstructor
public class UserController {
    private UserService userService;


    @PostMapping("/signup")
    public ResponseEntity<?> createUser(@RequestBody UserSignUpRequestDTO userSignUpRequest) {
        try {
            AuthUserRequestDTO authResponse = userService.createUser(userSignUpRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthUserResponseDTO> login(@RequestBody UserLoginRequestDTO loginRequest) {
        AuthUserResponseDTO authResponse = userService.login(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserData(@RequestBody UserSignUpUpdateRequestDTO userSignUpUpdateRequestDTO, @PathVariable Integer userId) {
        try {
            AuthUserResponseDTO authResponse = userService.updateUserData(userSignUpUpdateRequestDTO, userId);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUserById(@PathVariable Integer userId) {
        try {
            userService.deleteUserById(userId);
            return ResponseEntity.ok("User with ID " + userId + " deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }
}