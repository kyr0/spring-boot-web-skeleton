package com.mad2man.sbweb.user.api;

import com.mad2man.sbweb.user.aggregate.ManagedUserAggregate;
import com.mad2man.sbweb.user.service.UserService;
import com.mad2man.sbweb.user.viewmodel.UserViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
        notes = "returns all users",
        value = "find all users",
        response = UserViewModel.class,
        tags = {"user"})
    @PreAuthorize("hasAuthority('PERM_USERS_READ_ALL')")
    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ManagedUserAggregate>> users() {

        /* example of how to get the current user context:

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            UserContext userContext = (UserContext) authentication.getPrincipal();

            log.debug("userContext: " + userContext.getUsername());

         */

        final Page<ManagedUserAggregate> page = userService.getAllManagedUsers(null);

        return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
    }
}
