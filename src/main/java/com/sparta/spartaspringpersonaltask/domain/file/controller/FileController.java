package com.sparta.spartaspringpersonaltask.domain.file.controller;

import com.sparta.spartaspringpersonaltask.domain.file.entity.File;
import com.sparta.spartaspringpersonaltask.domain.file.service.FileService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public File uploadFile(@RequestParam("file") MultipartFile file) {
        return null;
    }
}
