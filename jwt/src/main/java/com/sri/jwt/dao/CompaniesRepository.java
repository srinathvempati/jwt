package com.sri.jwt.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sri.jwt.entity.SoftwareCompanies;


@Repository
public interface CompaniesRepository extends JpaRepository<SoftwareCompanies, Integer> {

}
