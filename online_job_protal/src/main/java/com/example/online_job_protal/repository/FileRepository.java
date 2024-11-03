package com.example.online_job_protal.repository;

import com.example.online_job_protal.Model.FileModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileModel, Long> {
    // You can define custom query methods here if needed
}
