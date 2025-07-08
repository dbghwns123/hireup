package com.project.hireup.repository;

import com.project.hireup.entity.Diary;
import com.project.hireup.entity.User;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiaryRepository extends JpaRepository<Diary, Long> {

  boolean existsByUserAndDate(User user, LocalDate date);
}
