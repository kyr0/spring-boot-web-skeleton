package com.mad2man.sbweb.user.api;

import com.mad2man.sbweb.common.api.response.Pagination;
import com.mad2man.sbweb.user.viewmodel.UserViewModel;
import com.mad2man.sbweb.user.service.UserService;
import com.mad2man.sbweb.user.aggregate.ManagedUserAggregate;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for users.
 */
@RestController
@Api(value = "UserEntity management api", basePath = "/api/user")
@Slf4j
public class UserResource {

    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @ApiOperation(
        notes = "returns all users paginated",
        value = "find all users",
        response = UserViewModel.class,
        tags = {"user"})
    @GetMapping(value = "/api/user/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedUserAggregate>> getAllUsers() {

        final Page<ManagedUserAggregate> page = userService.getAllManagedUsers(null);
        HttpHeaders headers = Pagination.generatePaginationHttpHeaders(page, "/api/users");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
