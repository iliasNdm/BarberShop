package org.example.barber_shop.service_layer.user.impl;

import org.example.barber_shop.dao.entities.User;
import org.example.barber_shop.dao.entities.UserRole;
import org.example.barber_shop.dao.repositories.UserRepository;
import org.example.barber_shop.dto.user.UserResponse;
import org.example.barber_shop.dto.user.UserUpdateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserManagerImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserManagerImpl userManager;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole(UserRole.CUSTOMER);
        user.setCreatedAt(new Date());
        user.setNewsletterSubscribed(false);
    }

    @Test
    void getUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse response = userManager.getUserById(1L);

        assertNotNull(response);
        assertEquals(user.getEmail(), response.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void getUserByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserResponse response = userManager.getUserByEmail("test@example.com");

        assertNotNull(response);
        assertEquals(user.getId(), response.getId());
        verify(userRepository, times(1)).findByEmail("test@example.com");
    }

    @Test
    void getAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<UserResponse> responses = userManager.getAllUsers();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(user.getEmail(), responses.get(0).getEmail());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void updateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserUpdateRequest request = new UserUpdateRequest();
        request.setFirstName("Jane");
        request.setLastName("Smith");
        request.setNewsletterSubscribed(true);

        UserResponse response = userManager.updateUser(1L, request);

        assertNotNull(response);
        assertEquals(request.getFirstName(), response.getFirstName());
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void deleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userManager.deleteUser(1L);

        verify(userRepository, times(1)).existsById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
