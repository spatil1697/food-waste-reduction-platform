package com.webapp.foodwastereductionplatform.service;

import com.webapp.foodwastereductionplatform.dto.*;
import com.webapp.foodwastereductionplatform.model.*;
import com.webapp.foodwastereductionplatform.model.User;
import com.webapp.foodwastereductionplatform.repositories.*;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.boot.test.context.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserServiceImplementation customUserDetails;
    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    void testCreateUser() throws Exception {
        // Prepare test data
        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setEmail("test@example.com");
        userSignUpRequestDTO.setPassword("password1@S");
        userSignUpRequestDTO.setFirstName("John");
        userSignUpRequestDTO.setLastName("Doe");
        userSignUpRequestDTO.setUserType("Individual");

        // Mocking repository response
        when(userRepository.findByEmail(userSignUpRequestDTO.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(userSignUpRequestDTO.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(new User());
        when(authentication.getName()).thenReturn(userSignUpRequestDTO.getEmail());

        // Invoke the method under test
        AuthUserRequestDTO authResponse = userService.createUser(userSignUpRequestDTO);
        Assertions.assertNotNull(authResponse);
        Assertions.assertTrue(authResponse.getStatus());
        Assertions.assertEquals("SignUp success", authResponse.getMessage());
        Assertions.assertNotNull(authResponse.getJwt());
    }


    @Test
    void testCreateUserWithEmailAlreadyExists() {
        // Prepare test data
        UserSignUpRequestDTO userSignUpRequestDTO = new UserSignUpRequestDTO();
        userSignUpRequestDTO.setEmail("test@example.com");

        // Mocking repository response
        when(userRepository.findByEmail(userSignUpRequestDTO.getEmail())).thenReturn(new User());

        // Invoke the method under test
        Assertions.assertThrows(Exception.class, () -> {
            userService.createUser(userSignUpRequestDTO);
        });
    }


    @Test
    void testLoginSuccess() {
        // Prepare test data
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("password");
        User user = new User();
        user.setEmail(loginRequestDTO.getEmail());
        user.setPassword("password");

        // Mocking repository response
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
        when(customUserDetails.loadUserByUsername(loginRequestDTO.getEmail())).thenReturn(userDetails);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())).thenReturn(true);

        // Invoke the method under test
        AuthUserResponseDTO authResponse = userService.login(loginRequestDTO);
        Assertions.assertNotNull(authResponse);
        Assertions.assertTrue(authResponse.getStatus());
        Assertions.assertEquals("Login success", authResponse.getMessage());
        Assertions.assertNotNull(authResponse.getJwt());
        Assertions.assertEquals(user.getFirstName(), authResponse.getFirstName());
        Assertions.assertEquals(user.getLastName(), authResponse.getLastName());
        Assertions.assertEquals(user.getAddress(), authResponse.getAddress());
        Assertions.assertEquals(user.getContactNumber(), authResponse.getContactNumber());
        Assertions.assertEquals(user.getUserType(), authResponse.getUserType());
        Assertions.assertEquals(user.getEmail(), authResponse.getEmail());
    }

    @Test
    void testLoginFailure() {
        // Prepare test data
        UserLoginRequestDTO loginRequestDTO = new UserLoginRequestDTO();
        loginRequestDTO.setEmail("test@example.com");
        loginRequestDTO.setPassword("wrong_password");
        User user = new User();
        user.setEmail(loginRequestDTO.getEmail());
        user.setPassword("password");

        // Mocking repository response
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoder.matches(loginRequestDTO.getPassword(), user.getPassword())).thenReturn(false);

        // Invoke the method under test
        Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.login(loginRequestDTO);
        });
    }


    @Test
    void testUpdateUserData() throws Exception {
        // Prepare test data
        Address address = new Address("street 123", "Berlin", "Berlin", "10115", "Germany");
        UserSignUpUpdateRequestDTO userSignUpUpdateRequestDTO = new UserSignUpUpdateRequestDTO();
        userSignUpUpdateRequestDTO.setLastName("Doe");
        userSignUpUpdateRequestDTO.setFirstName("John");
        userSignUpUpdateRequestDTO.setAddress(address);
        userSignUpUpdateRequestDTO.setContactNumber("1234567890");
        userSignUpUpdateRequestDTO.setUserType("Individual");
        Integer userId = 1;
        User user = new User();
        user.setId(userId);

        // Mocking repository response
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Invoke the method under test
        AuthUserResponseDTO authResponse = userService.updateUserData(userSignUpUpdateRequestDTO, userId);
        Assertions.assertNotNull(authResponse);
        Assertions.assertTrue(authResponse.getStatus());
        Assertions.assertEquals("User updated successfully", authResponse.getMessage());
        Assertions.assertEquals(userSignUpUpdateRequestDTO.getFirstName(), authResponse.getFirstName());
        Assertions.assertEquals(userSignUpUpdateRequestDTO.getLastName(), authResponse.getLastName());
        Assertions.assertEquals(userSignUpUpdateRequestDTO.getAddress(), authResponse.getAddress());
        Assertions.assertEquals(userSignUpUpdateRequestDTO.getContactNumber(), authResponse.getContactNumber());
        Assertions.assertEquals(userSignUpUpdateRequestDTO.getUserType(), authResponse.getUserType());
    }


    @Test
    void testUpdateUserDataWithInvalidUserType() {
        // Prepare test data
        UserSignUpUpdateRequestDTO userSignUpUpdateRequestDTO = new UserSignUpUpdateRequestDTO();
        userSignUpUpdateRequestDTO.setUserType("Invalid");
        User user = new User();
        Integer userId = 1;

        // Mocking repository response
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Invoke the method under test
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUserData(userSignUpUpdateRequestDTO, userId);
        });
    }

   
    @Test
    void testGetUserData() {

        // Prepare test data
        Integer userId = 1;
        User user = new User();
        Address address = new Address("Street 123", "Berlin", "Berlin", "10115", "Germany");
        user.setId(userId);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setAddress(address);
        user.setContactNumber("1234567890");
        user.setUserType("admin");
        user.setEmail("john.doe@example.com");

        // Mocking repository response
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Invoke the method under test
        AuthUserResponseDTO result = userService.getUserData(userId);

        // Verify repository method call
        verify(userRepository, times(1)).findById(userId);

        // Assert the returned user data
        assertNotNull(result);
        assertEquals("User updated successfully", result.getMessage());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        Address resultAddress = result.getAddress();
        assertNotNull(resultAddress);
        assertEquals("Street 123", resultAddress.getStreetAddress());
        assertEquals("Berlin", resultAddress.getCity());
        assertEquals("Berlin", resultAddress.getState());
        assertEquals("10115", resultAddress.getPostalCode());
        assertEquals("Germany", resultAddress.getCountry());
    
        assertEquals("1234567890", result.getContactNumber());
        assertEquals("admin", result.getUserType());
        assertEquals("john.doe@example.com", result.getEmail());
    }


    @Test
    void testDeleteUserById() {
        // Prepare test data
        String email = "sam@gmail.com";
        User user = new User();
        user.setEmail(email);
        user.setPassword("safaffsfs");

        // Mocking repository response
        when(userRepository.findByEmail(email)).thenReturn((user));

        // Invoke the method under test
        userRepository.delete(user);

        // Verify repository method call
        Mockito.verify(userRepository, Mockito.times(1)).delete(user);
    }

    @Test
    void testDeleteUserWithNonExistingUser() {
        String email = "sam@gmail.com";
        String password = "adaddsd";

        // Mocking repository response
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Invoke the method under test and assert that an exception is thrown
        BadCredentialsException thrown = Assertions.assertThrows(BadCredentialsException.class, () -> {
            userService.deleteUser(email, password);
        });

        // Assert that the exception message is correct
        Assertions.assertEquals("Invalid email and password", thrown.getMessage());
    }
}
