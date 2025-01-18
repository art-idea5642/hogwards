package com.art.hogwards_students.services;

import com.art.hogwards_students.model.Avatar;
import com.art.hogwards_students.model.Student;
import com.art.hogwards_students.repositories.AvatarRepository;
import com.art.hogwards_students.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {
    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);
    private final StudentRepository studentRepository;
    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.avatarRepository = avatarRepository;
    }


    @Value("${path.to.avatars.folder}")
    private String avatarsDir;


    public Avatar findAvatar(long studentId) {
        logger.info("Was invoked method for find avatar by student id");
        logger.debug("Finding avatar for student with id: {}",studentId);
        return avatarRepository.findByStudentId(studentId).orElseThrow(() -> new ResponseStatusException
                (HttpStatus.NOT_FOUND, "Student not found with id: " + studentId));
    }

    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");

        Student student = studentRepository.getById(studentId);

        Path filePath = Path.of(avatarsDir, studentId + "." + getExtensions(avatarFile.getOriginalFilename()));

        try {
            Files.createDirectories(filePath.getParent());
            Files.deleteIfExists(filePath);

            try (InputStream is = avatarFile.getInputStream();
                 OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                 BufferedInputStream bis = new BufferedInputStream(is, 1024);
                 BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
                bis.transferTo(bos);
            }
            logger.info("Avatar file successfully uploaded for student with id: {}", studentId);
        } catch (IOException e) {
            logger.error("IOException occurred during file upload for student with id: {}", studentId, e);
            throw e;
        }

        Avatar avatar;
        try {
            logger.debug("Attempting to find existing avatar for student with id: {}", studentId);
            avatar = findAvatar(studentId);
        } catch (ResponseStatusException e) {
            logger.warn("No existing avatar found for student with id: {}", studentId);
            avatar = new Avatar();
        }

        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());

        try {
            logger.debug("Saving avatar to repository for student with id: {}", studentId);
            avatarRepository.save(avatar);
        } catch (Exception e) {
            logger.error("Error saving avatar for student with id: {}", studentId, e);
            throw e;
        }
    }

    private String getExtensions(String fileName) {
        logger.info("Was invoked method for get image extensions");
        logger.debug("Extracting file extension from filename: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public List<Avatar> getAvatars(int page, int size) {
        logger.info("Was invoked method for get avatars");

        PageRequest pageRequest = PageRequest.of(page - 1, size);

        List<Avatar> avatars = avatarRepository.findAll(pageRequest).getContent();
        logger.debug("Found {} avatars", avatars.size());

        return avatars;
    }



}
