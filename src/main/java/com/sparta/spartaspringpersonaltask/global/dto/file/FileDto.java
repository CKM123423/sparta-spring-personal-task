package com.sparta.spartaspringpersonaltask.global.dto.file;

import com.sparta.spartaspringpersonaltask.domain.file.entity.File;
import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import lombok.Getter;

@Getter
public class FileDto {
    private String fileTitle;
    private String fileExtension;
    private int fileSize;
    private byte[] fileContent;

    public File toEntity(Schedule schedule) {
        return File.builder()
                .fileTitle(this.fileTitle)
                .fileExtension(this.fileExtension)
                .fileSize(this.fileSize)
                .fileContent(this.fileContent)
                .schedule(schedule)
                .build();
    }
}