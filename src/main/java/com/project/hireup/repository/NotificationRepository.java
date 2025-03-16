package com.project.hireup.repository;

import com.project.hireup.entity.Notification;
import com.project.hireup.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  Page<Notification> findAllByUser(User user, Pageable pageable);
}
