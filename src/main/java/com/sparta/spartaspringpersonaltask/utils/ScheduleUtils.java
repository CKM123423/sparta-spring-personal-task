package com.sparta.spartaspringpersonaltask.utils;

import com.sparta.spartaspringpersonaltask.entity.Schedule;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.exceptions.customexceptions.InvalidPasswordException;

import java.util.Objects;

public class ScheduleUtils {

    // 일정 삭제 여부 확인
    public static void checkDeletionStatus(Schedule schedule) {
        if (schedule.isDeletionStatus()) {
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }
    }

    // 비밀번호 일치 확인
    public static void checkPassword(String inputPassword, String storedPassword) {
        if (!Objects.equals(inputPassword, storedPassword)) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}
