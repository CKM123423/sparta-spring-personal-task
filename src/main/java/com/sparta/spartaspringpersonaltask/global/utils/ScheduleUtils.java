package com.sparta.spartaspringpersonaltask.global.utils;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.AlreadyDeletedException;
import com.sparta.spartaspringpersonaltask.global.exception.customexceptions.InvalidException;

import java.util.Objects;

// 서비스가 방대해지고 여러 서비스 클래스에서 같은 역할을 하는 애들만 정적 유틸클래스로 선언하는것이 좋음
// 현재는 서비스가 작아서 유틸 클래스의 의미가 퇴색됨
@Deprecated
public class ScheduleUtils {

    // 일정 삭제 여부 확인
    public static void checkDeletionStatus(Schedule schedule) {
        if (schedule.getDeletionStatus() != null) {
            throw new AlreadyDeletedException("이미 삭제된 일정입니다.");
        }
    }

    // 비밀번호 일치 확인
    public static void checkPassword(String inputPassword, String storedPassword) {
        if (!Objects.equals(inputPassword, storedPassword)) {
            throw new InvalidException("비밀번호가 일치하지 않습니다.");
        }
    }
}
