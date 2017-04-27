package com.mad2man.sbweb.rest;

import com.mad2man.sbweb.rest.model.UserModel;
import com.mad2man.sbweb.rest.util.PaginationUtil;
import com.mad2man.sbweb.service.UserService;
import com.mad2man.sbweb.service.dto.UserDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for users.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);


    private final UserService userService;

    @Autowired
    public UserResource(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all users.
     * GET  /users : get all users.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @ApiOperation(notes = "returns all users paginated", value = "find all users", response = UserModel.class,
        tags = {"user"}, authorizations = {
        @Authorization(value = "petstore_auth", scopes = {
            @AuthorizationScope(scope = "read:user", description = "read all users")
        })})
    @RequestMapping(value = "/users", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        final Page<UserDTO> page = userService.getAllManagedUsers(null);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
