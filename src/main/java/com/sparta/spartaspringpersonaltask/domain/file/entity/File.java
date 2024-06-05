package com.sparta.spartaspringpersonaltask.domain.file.entity;

import com.sparta.spartaspringpersonaltask.domain.schedule.entity.Schedule;
import com.sparta.spartaspringpersonaltask.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "files")
public class File extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @Column(nullable = false)
    private String fileTitle;

    @Column(nullable = false)
    private String fileExtension;

    @Column(nullable = false)
    private int fileSize;

    @Lob
    @Column(nullable = false, columnDefinition = "BLOB")
    private byte[] fileContent;

    private LocalDateTime fileDeleteAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @Builder
    public File(String fileTitle, String fileExtension, int fileSize, byte[] fileContent, Schedule schedule) {
        this.fileTitle = fileTitle;
        this.fileExtension = fileExtension;
        this.fileSize = fileSize;
        this.fileContent = fileContent;
        this.fileDeleteAt = null;
        this.schedule = schedule;
    }

    public void deletedTime() {
        this.fileDeleteAt = LocalDateTime.now();
    }
}
