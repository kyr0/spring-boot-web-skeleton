package com.mad2man.sbweb.user.api;

import com.mad2man.sbweb.common.api.response.Pagination;
import com.mad2man.sbweb.user.model.ManagedUserModel;
import com.mad2man.sbweb.user.service.UserService;
import com.mad2man.sbweb.user.service.dto.ManagedUserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for users.
 */
@RestController
@RequestMapping("/api/users")
@Api(value = "User management api", basePath = "/api/user")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);


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
        response = ManagedUserModel.class,
        tags = {"user"})
    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedUserDTO>> getAllUsers() {

        final Page<ManagedUserDTO> page = userService.getAllManagedUsers(null);
        HttpHeaders headers = Pagination.generatePaginationHttpHeaders(page, "/api/users");

        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }
}
