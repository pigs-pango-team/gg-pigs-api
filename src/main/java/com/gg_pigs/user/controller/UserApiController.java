package com.gg_pigs.user.controller;

import com.gg_pigs._common.dto.ApiResponse;
import com.gg_pigs._common.utility.JwtProvider;
import com.gg_pigs.user.dto.CreateDtoUser;
import com.gg_pigs.user.dto.RetrieveDtoUser;
import com.gg_pigs.user.dto.UpdateDtoUser;
import com.gg_pigs.user.service.UserService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class UserApiController {

    private final JwtProvider jwtProvider;
    private final UserService userService;

    /**
     * CREATE
     * */
    @PostMapping("/api/v1/users")
    public ApiResponse createOneUser(@RequestBody CreateDtoUser createDtoUser) {
        Long userId = userService.createOneUser(createDtoUser);

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userId);
    }

    /**
     * RETRIEVE
     * */
    @GetMapping("/api/v1/users/{userId}")
    public ApiResponse retrieveOneUser(@PathVariable("userId") Long _userId) {
        RetrieveDtoUser retrieveDtoUser = userService.retrieveOneUser(_userId);

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), retrieveDtoUser);
    }

    @GetMapping("/api/v1/login-users")
    public ApiResponse retrieveOneUserByToken(@CookieValue("jwt") String token) {
        Claims payload = jwtProvider.getPayloadFromToken(token);
        String userEmail = payload.getAudience();

        RetrieveDtoUser retrieveDtoUser = userService.retrieveOneUserByEmail(userEmail);

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), retrieveDtoUser);
    }

    @GetMapping("/api/v1/users")
    public ApiResponse retrieveAllUser() {
        List<RetrieveDtoUser> allRetrieveDtoUsers = userService.retrieveAllUser();

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), allRetrieveDtoUsers);
    }

    /**
     * UPDATE
     * */
    @PutMapping("/api/v1/users/{userId}")
    public ApiResponse updateOneUser(@PathVariable("userId") Long _userId, @RequestBody UpdateDtoUser updateDtoUser) {
        Long userId = userService.updateOneUser(_userId, updateDtoUser);

        return new ApiResponse(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), userId);
    }

    /**
     * DELETE
     * */
    @DeleteMapping("/api/v1/users/{userId}")
    public ApiResponse deleteOneUser(@PathVariable("userId") Long _userId) {
        userService.deleteOneUser(_userId);

        return new ApiResponse(HttpStatus.NO_CONTENT.value(), HttpStatus.NO_CONTENT.getReasonPhrase(), new ArrayList<>());
    }
}