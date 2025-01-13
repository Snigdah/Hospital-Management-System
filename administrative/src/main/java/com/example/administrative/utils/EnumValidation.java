package com.example.administrative.utils;

import com.example.administrative.exception.CustomeException;
import com.example.administrative.model.Enum.RoomType;
import org.springframework.http.HttpStatus;

public class EnumValidation {
    public static RoomType parseRoomType(String roomTypeStr) {
        try {
            return RoomType.valueOf(roomTypeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new CustomeException(HttpStatus.BAD_REQUEST, "Invalid room type. Supported room type are SINGLE and DOUBLE");
        }
    }
}
