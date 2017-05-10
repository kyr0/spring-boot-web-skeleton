package com.mad2man.sbweb.heartbeat.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class HeartbeatViewModel {

    private LocalDateTime timestamp;
}
