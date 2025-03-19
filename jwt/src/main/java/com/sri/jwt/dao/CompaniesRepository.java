package com.sri.jwt.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sri.jwt.entity.SoftwareCompanies;
import com.sri.jwt.entity.User;
import com.sri.jwt.entity.WeatherReport;


@Repository
public interface CompaniesRepository extends JpaRepository<SoftwareCompanies, Integer> {
	
	@Query("SELECT c FROM SoftwareCompanies c WHERE c.company_name = :company_name")
    Optional<SoftwareCompanies> findByCompanyName(@Param("company_name") String company_name);
	
	@Query("SELECT c FROM SoftwareCompanies c WHERE c.company_name LIKE %:company_name%")
    List<SoftwareCompanies> findByCompaniesNames(@Param("company_name") String company_name);
	
	@Query("SELECT s.company_name, s.location FROM SoftwareCompanies s")
    List<Object[]> findCompanyNamesAndLocations();
}
