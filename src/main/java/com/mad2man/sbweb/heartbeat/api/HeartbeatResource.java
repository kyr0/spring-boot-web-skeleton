package com.mad2man.sbweb.heartbeat.api;

import com.mad2man.sbweb.heartbeat.viewmodel.HeartbeatViewModel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

/**
 * Simple heartbeat endpoint
 */
@RestController
@Api(value = "Heartbeat service", basePath = "/api/heartbeat")
@Slf4j
public class HeartbeatResource {

    /**
     * Returns a heartbeat
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(
        notes = "returns a heartbeat",
        value = "don't skip a beat",
        response = HeartbeatViewModel.class,
        tags = {"heartbeat"}
    )
    @PreAuthorize("permitAll()") // public
    @GetMapping(value = "/api/heartbeat", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HeartbeatViewModel> heartbeat() {
        return new ResponseEntity<>(new HeartbeatViewModel(LocalDateTime.now()), HttpStatus.OK);
    }
}
