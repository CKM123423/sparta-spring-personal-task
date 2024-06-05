package com.sparta.spartaspringpersonaltask.domain.file.repository;

import com.sparta.spartaspringpersonaltask.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
