package com.mad2man.sbweb.resource;

import com.mad2man.sbweb.model.UserModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Mansi on 26.04.2017.
 */
@RestController
public class UserResource {

    @ApiOperation(notes = "Returns a user queried by user id", value = "Find user by ID", response = UserModel.class,
            tags = {"user"})
    @RequestMapping(value = "/api/users/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserModel> getPetById(@ApiParam(value = "ID of user that needs to be fetched", allowableValues = "range[1,*]", required = true) @PathVariable("id") Integer userId) throws Exception {
        UserModel userModel = new UserModel();
        userModel.setId(userId);
        return ResponseEntity.ok().body(userModel);

    }

}
