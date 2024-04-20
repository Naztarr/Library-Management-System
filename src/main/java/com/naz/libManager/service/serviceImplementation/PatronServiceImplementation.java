package com.naz.libManager.service.serviceImplementation;

import com.naz.libManager.dto.UserRequest;
import com.naz.libManager.entity.User;
import com.naz.libManager.enums.Role;
import com.naz.libManager.exception.LibManagerException;
import com.naz.libManager.mapper.UserMapper;
import com.naz.libManager.payload.ApiResponse;
import com.naz.libManager.payload.PatronDetail;
import com.naz.libManager.payload.UserData;
import com.naz.libManager.repository.UserRepository;
import com.naz.libManager.service.PatronService;
import com.naz.libManager.util.UserUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.beans.support.PagedListHolder.DEFAULT_PAGE_SIZE;

/**
 * Implementation of PatronService providing operations related to patrons.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "patron")
public class PatronServiceImplementation implements PatronService {
    private final UserRepository userRepository;


    /**
     * Retrieves a list of all patrons with pagination.
     *
     * @param page The page number
     * @param size The size of each page
     * @return ResponseEntity containing ApiResponse with a list of UserData representing patrons
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable
    public ResponseEntity<ApiResponse<List<UserData>>> getAllPatrons(Integer page, Integer size) {
        page = page != null && page >= 0 ? page : 0;
        size = size != null && size > 0 ? size : DEFAULT_PAGE_SIZE;

        Pageable pageable = PageRequest.of(page, size);
        userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));

            Page<User> allPatrons = userRepository.findAllPatrons(pageable);
            return ResponseEntity.ok(new ApiResponse<>(
                    allPatrons.stream()
                            .map(patron -> UserMapper.mapUserToUserData(new UserData(), patron))
                            .collect(Collectors.toList()), "All patrons successfully fetched"
            ));
        }

    /**
     * Retrieves detailed information about a specific patron.
     *
     * @param patronId The UUID of the patron
     * @return ResponseEntity containing ApiResponse with the PatronDetail
     */
    @Override
    @Transactional(readOnly = true)
    @Cacheable(key = "#patronId")
    public ResponseEntity<ApiResponse<PatronDetail>> viewPatronDetail(UUID patronId) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));

        if(user.getRole() == Role.ADMIN){
            User patron = userRepository.findById(patronId)
                    .orElseThrow(() -> new LibManagerException("Patron does not exist"));
            return ResponseEntity.ok(new ApiResponse<>(UserMapper
                    .mapUserToPatronDetail(new PatronDetail(), patron),
                    "patron details successfully fetched"));
        } else{
            throw new LibManagerException("You are not authorized to view this information");
        }
    }

    /**
     * Updates the details of a specific patron.
     *
     * @param patronId     The UUID of the patron
     * @param userRequest  The UserRequest containing updated details
     * @return ResponseEntity containing ApiResponse confirming the update
     */
    @Override
    @Transactional
    @CachePut(key = "#patronId")
    public ResponseEntity<ApiResponse<String>> updatePatronDetail(UUID patronId, UserRequest userRequest) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        User patron = userRepository.findById(patronId)
                .orElseThrow(() -> new LibManagerException("User not found"));
        if(user.equals(patron)){
            userRepository.save(UserMapper.mapUserRequestToUser(user, userRequest));
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity
                .ok(new ApiResponse<>("Your details are successfully updated", HttpStatus.OK));
    }

    /**
     * Removes a specific patron.
     *
     * @param patronId The UUID of the patron to be removed
     * @return ResponseEntity containing ApiResponse confirming the removal
     */
    @Override
    @Transactional
    @CacheEvict(key = "#patronId")
    public ResponseEntity<ApiResponse<String>> removePatron(UUID patronId) {
        User user = userRepository.findByEmailAddress(UserUtil.getLoginUser())
                .orElseThrow(() -> new LibManagerException("User not found"));
        User patron = userRepository.findById(patronId)
                .orElseThrow(() -> new LibManagerException("User not found"));
        if(user.equals(patron)){
            userRepository.deleteById(patronId);
        } else if(user.getRole() == Role.ADMIN){
            userRepository.delete(patron);
            return ResponseEntity
                    .ok(new ApiResponse<>("Patron successfully removed", HttpStatus.OK));
        } else{
            throw new LibManagerException("You are not authorized to do this");
        }
        return ResponseEntity
                .ok(new ApiResponse<>("You have been removed", HttpStatus.OK));
    }
}
