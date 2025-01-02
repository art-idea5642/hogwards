package com.art.hogwards_students.repositories;

import com.art.hogwards_students.model.Avatar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional <Avatar> findByStudentId(long studentId);
}
