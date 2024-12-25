package com.youtube.jwt.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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

import com.youtube.jwt.dao.CompaniesRepository;
import com.youtube.jwt.entity.SoftwareCompanies;
import com.youtube.jwt.exception.CompanyNameNotFoundException;


@RestController
public class CompaniesResource {

	private CompaniesRepository companiesRepository;

	public CompaniesResource(CompaniesRepository companiesRepository) {
		this.companiesRepository = companiesRepository;
	}

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
	
	//update Company
		@PutMapping(path = "/software/companies/{id}")
		public SoftwareCompanies updateCompany(@PathVariable int id, @RequestBody SoftwareCompanies softwareCompanies  ) {
			
			companiesRepository.save(softwareCompanies);
			
			return softwareCompanies;
		}

	// adding new Company
	@PostMapping(path = "/software/companies")
	public ResponseEntity<SoftwareCompanies> createNewCompany( @RequestBody SoftwareCompanies softwareCompanies) {
		SoftwareCompanies savedCompany = companiesRepository.save(softwareCompanies);

		// Below URI logic we are creating(inserting) and getting the ID of the user
		// which we are inserting.

		/*
		 * 1. when ever we want to return URL of a created user we need to use location,
		 * ResponseEntity.created(location) this will give us location. 2. URI location
		 * = /tech/user/(id) 3. ServletUriComponentsBuilder.fromCurrentRequest() -> this
		 * will give us url of current request for this we are appending {ID}
		 */

		URI location = ServletUriComponentsBuilder.fromCurrentRequest() // to uri of the current request
				.path("/{id}") // adding path
				.buildAndExpand(savedCompany.getId()) // replace above path ({id}) with id of created user
				.toUri(); // convert into URI and return back

		return ResponseEntity.created(location).build();

		// check in POSTMAN response you will get newly created user URI with ID.
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
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }

}
