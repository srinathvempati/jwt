package com.sri.jwt.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.sri.jwt.dao.CompaniesRepository;
import com.sri.jwt.entity.SoftwareCompanies;
import com.sri.jwt.entity.User;
import com.sri.jwt.exception.CompanyNameNotFoundException;

@RestController
public class CompaniesResource {

	private CompaniesRepository companiesRepository;

	public CompaniesResource(CompaniesRepository companiesRepository) {
		this.companiesRepository = companiesRepository;
	}

	// GET all companies
	@GetMapping(path = "/software/companies")
	public List<SoftwareCompanies> retrieveAllCompanies() {
		return companiesRepository.findAll();
	}

	// get specific company details
	@GetMapping(path = "/software/companies/{id}")
	public EntityModel<SoftwareCompanies> retrieveCompany(@PathVariable int id) {
		Optional<SoftwareCompanies> softwareCompanies = companiesRepository.findById(id);

		if (softwareCompanies.isEmpty()) {
			throw new CompanyNameNotFoundException("id:" + id);
		}

		EntityModel<SoftwareCompanies> entityModel = EntityModel.of(softwareCompanies.get());

		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllCompanies());
		entityModel.add(link.withRel("all-companies"));
		return entityModel;

	}

	// get specific company details
	@GetMapping(path = "/software/companies/detail/{companyName}")
	public List<SoftwareCompanies> retrieveCompanyName(@PathVariable String companyName) {

		return companiesRepository.findByCompaniesNames(companyName);

	}

	// update Company
	@PutMapping(path = "/software/companies/{id}")
	public SoftwareCompanies updateCompany(@PathVariable int id, @RequestBody SoftwareCompanies softwareCompanies) {

		companiesRepository.save(softwareCompanies);

		return softwareCompanies;
	}

	// adding new Company
	@PostMapping(path = "/software/companies")
	public ResponseEntity<?> createNewCompany(@RequestBody SoftwareCompanies softwareCompanies) {
		// Check if a company with the same name already exists
		Optional<SoftwareCompanies> existingCompany = companiesRepository
				.findByCompanyName(softwareCompanies.getCompany_name());

		if (existingCompany.isPresent()) {
			// Return a response indicating that the company already exists
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Company with name '" + softwareCompanies.getCompany_name() + "' already exists.");
		}

		// Save the new company if the name doesn't exist
		SoftwareCompanies savedCompany = companiesRepository.save(softwareCompanies);

		return ResponseEntity.status(HttpStatus.CREATED).body(savedCompany);
	}

	// delete company
	@DeleteMapping(path = "/software/companies/{id}")
	@PreAuthorize("hasRole('Admin')")
	public void deleteUser(@PathVariable int id) {
		companiesRepository.deleteById(id);

	}

	@GetMapping("/whoami")
	public String whoAmI() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && authentication.isAuthenticated()) {
			return authentication.getName(); // Returns the username from the JWT
		} else {
			return "Anonymous";
		}
	}

	@GetMapping("/roles")
	public Collection<String> getRoles() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toList());
	}

}
