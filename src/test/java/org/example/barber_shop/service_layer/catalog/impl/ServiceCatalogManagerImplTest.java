package org.example.barber_shop.service_layer.catalog.impl;

import org.example.barber_shop.dao.entities.Service;
import org.example.barber_shop.dao.repositories.ServiceRepository;
import org.example.barber_shop.dto.service.ServiceCreationRequest;
import org.example.barber_shop.dto.service.ServiceResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServiceCatalogManagerImplTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceCatalogManagerImpl serviceCatalogManager;

    private Service service;
    private ServiceCreationRequest request;

    @BeforeEach
    void setUp() {
        service = new Service();
        service.setId(1L);
        service.setName("Test Service");
        service.setDurationMinutes(30);
        service.setPrice(20.0);

        request = new ServiceCreationRequest();
        request.setName("Test Service");
        request.setDurationMinutes(30);
        request.setPrice(20.0);
    }

    @Test
    void createService() {
        when(serviceRepository.save(any(Service.class))).thenReturn(service);

        ServiceResponse response = serviceCatalogManager.createService(request);

        assertNotNull(response);
        assertEquals(request.getName(), response.getName());
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    void updateService() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));
        when(serviceRepository.save(any(Service.class))).thenReturn(service);

        ServiceCreationRequest updateRequest = new ServiceCreationRequest();
        updateRequest.setName("Updated Service");
        updateRequest.setDurationMinutes(45);
        updateRequest.setPrice(30.0);

        ServiceResponse response = serviceCatalogManager.updateService(1L, updateRequest);

        assertNotNull(response);
        assertEquals(updateRequest.getName(), response.getName());
        verify(serviceRepository, times(1)).findById(1L);
        verify(serviceRepository, times(1)).save(any(Service.class));
    }

    @Test
    void deleteService() {
        when(serviceRepository.existsById(1L)).thenReturn(true);
        doNothing().when(serviceRepository).deleteById(1L);

        serviceCatalogManager.deleteService(1L);

        verify(serviceRepository, times(1)).existsById(1L);
        verify(serviceRepository, times(1)).deleteById(1L);
    }

    @Test
    void getServiceById() {
        when(serviceRepository.findById(1L)).thenReturn(Optional.of(service));

        ServiceResponse response = serviceCatalogManager.getServiceById(1L);

        assertNotNull(response);
        assertEquals(service.getName(), response.getName());
        verify(serviceRepository, times(1)).findById(1L);
    }

    @Test
    void getAllServices() {
        when(serviceRepository.findAll()).thenReturn(Collections.singletonList(service));

        List<ServiceResponse> responses = serviceCatalogManager.getAllServices();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(service.getName(), responses.get(0).getName());
        verify(serviceRepository, times(1)).findAll();
    }
}
