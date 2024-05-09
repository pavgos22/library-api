package com.projects.library.repository;

import com.projects.library.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;



public interface TitleRepository extends JpaRepository<Title, Integer> {
}
