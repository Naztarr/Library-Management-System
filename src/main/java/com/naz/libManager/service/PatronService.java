package com.naz.libManager.service;

import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface PatronService {

    ResponseEntity<ApiResponse<List<UserData>>> getAllPatrons(Integer page, Integer size);
    ResponseEntity<ApiResponse<PatronDetail>> viewPatronDetail(UUID patronId);
    ResponseEntity<ApiResponse<String>> updatePatronDetail(UUID patronId, UserRequest userRequest);
    ResponseEntity<ApiResponse<String>> removePatron(UUID patronId);
}
