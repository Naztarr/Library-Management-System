package com.naz.libManager.controller;

import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;
import com.naz.libManager.service.PatronService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(
        name = "Patrons",
        description = "REST APIs for managing patrons in LibManager"
)
public class PatronController {
    private final PatronService patronService;

    @GetMapping("/patrons")
    @Operation(summary = "Get list of patrons",
            description = "Retrieve a list of all patrons with pagination support")
    public ResponseEntity<ApiResponse<List<UserData>>> getPatrons(@RequestParam(defaultValue = "0") Integer page,
                                                                  @RequestParam(defaultValue = "10") Integer size){
        return ResponseEntity.ok(patronService.getAllPatrons(page, size).getBody());
    }

    @GetMapping("/patrons/{id}")
    @Operation(summary = "Get patron details",
            description = "Retrieve details of a specific patron by ID")
    public ResponseEntity<ApiResponse<PatronDetail>> getPatronDetail(@PathVariable UUID id){
        return ResponseEntity.ok(patronService.viewPatronDetail(id).getBody());
    }

    @PutMapping("/patrons/{id}")
    @Operation(summary = "Update patron details",
            description = "Update details of a specific patron by ID")
    public ResponseEntity<ApiResponse<String>> updatePatronDetail(@PathVariable UUID id, @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(patronService.updatePatronDetail(id, userRequest).getBody());
    }

    @DeleteMapping("/patrons/{id}")
    @Operation(summary = "Remove patron",
            description = "Remove a specific patron by ID")
    public ResponseEntity<ApiResponse<String>> removePatron(@PathVariable UUID id){
        return ResponseEntity.ok(patronService.removePatron(id).getBody());
    }
}
