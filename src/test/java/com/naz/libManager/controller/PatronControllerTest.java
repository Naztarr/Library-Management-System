package com.naz.libManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;
import com.naz.libManager.service.serviceImplementation.JwtImplementation;
import com.naz.libManager.service.serviceImplementation.PatronServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = PatronController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class PatronControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PatronServiceImplementation patronService;

    @MockBean
    JwtImplementation jwtImplementation;

    @MockBean
    UserDetailsService userDetailsService;

@Test
void getPatrons() throws Exception {
    // Mock data
    List<UserData> patronList = new ArrayList<>();
    patronList.add(new UserData());
    patronList.add(new UserData());
    patronList.add(new UserData());

    // Mock response entity
    ResponseEntity<ApiResponse<List<UserData>>> returnResponse = ResponseEntity.ok()
            .body(new ApiResponse<>(patronList, "All patrons successfully fetched"));

    // Mock service method call
    when(patronService.getAllPatrons(anyInt(), anyInt())).thenReturn(returnResponse);

    // Act and Assert
    mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.message").value("All patrons successfully fetched"))
            .andExpect(jsonPath("$.data").isArray())
            .andExpect(jsonPath("$.data.length()").value(patronList.size()));

    // Verify that patronService.getAllPatrons is called with the correct arguments
    verify(patronService).getAllPatrons(anyInt(), anyInt());
}

    @Test
    void getPatronDetail() throws Exception {
        // Mock data
        PatronDetail patronDetail = new PatronDetail();
        patronDetail.setFirstName("Naz");
        patronDetail.setLastName("Ozo");
        patronDetail.setEmailAddress("Naz@gmail.com");

        UUID patronId = UUID.randomUUID();

        // Mock response entity
        ResponseEntity<ApiResponse<PatronDetail>> returnResponse = ResponseEntity.ok()
                .body(new ApiResponse<>(patronDetail, "Patron details successfully fetched"));

        // Mock service method call
        when(patronService.viewPatronDetail(any(UUID.class))).thenReturn(returnResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/patrons/{id}", patronId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Patron details successfully fetched"))
                .andExpect(jsonPath("$.data").exists());

        // Verify that patronService.viewPatronDetail is called with the correct argument
        verify(patronService).viewPatronDetail(any(UUID.class));
    }

    @Test
    void updatePatronDetail() throws Exception {
        // Mock data
        UUID id = UUID.randomUUID();
        UserRequest userRequest = new UserRequest("Naz", "Ozo", "Naz@gmail.com", "08063746793");
        // Mock response entity
        ResponseEntity<ApiResponse<String>> returnResponse = ResponseEntity.ok()
                .body(new ApiResponse<>( "Patron details successfully updated", HttpStatus.OK));

        // Mock service method call
        when(patronService.updatePatronDetail(any(UUID.class), any(UserRequest.class))).thenReturn(returnResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.put("/api/patrons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Patron details successfully updated"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Verify that patronService.updatePatronDetail is called with the correct arguments
        verify(patronService).updatePatronDetail(any(UUID.class), any(UserRequest.class));
    }

    @Test
    void removePatron() throws Exception {
        // Mock data
        UUID id = UUID.randomUUID();

        // Mock response entity
        ResponseEntity<ApiResponse<String>> returnResponse = ResponseEntity.ok()
                .body(new ApiResponse<>("Patron removed successfully", HttpStatus.OK));

        // Mock service method call
        when(patronService.removePatron(any(UUID.class))).thenReturn(returnResponse);

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/patrons/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Patron removed successfully"))
                .andExpect(jsonPath("$.data").doesNotExist());

        // Verify that patronService.removePatron is called with the correct arguments
        verify(patronService).removePatron(any(UUID.class));
    }
}