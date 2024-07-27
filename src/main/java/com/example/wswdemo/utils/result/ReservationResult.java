package com.example.wswdemo.utils.result;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Data
public class ReservationResult {

    private Integer code;

    private LocalDateTime startTime;

    private LocalDateTime endTime;


    public static ReservationResult error(LocalDateTime startTime,LocalDateTime endTime, Integer code) {
        ReservationResult reservationResult = new ReservationResult();
        reservationResult.code = code;
        reservationResult.startTime = startTime;
        reservationResult.endTime = endTime;
        return  reservationResult;
    }

    public static ReservationResult success(Integer code) {
        ReservationResult reservationResult = new ReservationResult();
        reservationResult.code = code;
        return reservationResult;
    }
}
