package org.sample.repository;

import org.sample.entities.ProjectSpecs;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<ProjectSpecs, Long> {
    
}
