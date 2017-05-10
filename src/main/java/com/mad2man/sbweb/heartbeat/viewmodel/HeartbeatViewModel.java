package com.mad2man.sbweb.heartbeat.viewmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class HeartbeatViewModel {

    private LocalDateTime timestamp;
}
