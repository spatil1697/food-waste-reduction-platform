package com.webapp.foodwastereductionplatform.controller;


import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.service.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal Server Error"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDTO loginRequest) {
        try {
            AuthUserResponseDTO authResponse = userService.login(loginRequest);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException | UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal Server Error"));
        }
    }


    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUserData(@RequestBody UserSignUpUpdateRequestDTO userSignUpUpdateRequestDTO, @PathVariable Integer userId) {
        try {
            AuthUserResponseDTO authResponse = userService.updateUserData(userSignUpUpdateRequestDTO, userId);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal Server Error"));
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserData(@PathVariable Integer userId) {
        try {
            AuthUserResponseDTO authResponse = userService.getUserData(userId);
            return ResponseEntity.ok(authResponse);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Internal Server Error"));
        }
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(  @RequestParam String email, @RequestParam String password) {
        try {
            userService.deleteUser(email, password);
            return ResponseEntity.ok().body(new ApiResponse(true, "User with ID " + email + " deleted successfully."));
        }catch (IllegalArgumentException | UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete user.");
        }
    }
}