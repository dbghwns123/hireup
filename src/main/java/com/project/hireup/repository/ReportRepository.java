package com.project.hireup.repository;

import com.project.hireup.entity.Report;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {


  List<Report> findAllByReporterId(Long userId);
}
