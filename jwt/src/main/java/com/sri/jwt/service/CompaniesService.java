package com.sri.jwt.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sri.jwt.dao.CompaniesRepository;
import com.sri.jwt.entity.WeatherReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CompaniesService {

	private static final Logger logger = LoggerFactory.getLogger(CompaniesService.class);

	private final CompaniesRepository companiesRepository;
	private final RestTemplate restTemplate;
	
	

	private static final String API_KEY = "378af82c6ce1140058462e85644594fd";
	private static final String API_URL = "https://api.openweathermap.org/data/2.5/weather?q={location}&units=metric&appid={apiKey}";

	public CompaniesService(CompaniesRepository companiesRepository, RestTemplate restTemplate) {
		this.companiesRepository = companiesRepository;
		this.restTemplate = restTemplate;
	}

	public List<WeatherReport> getWeatherDetailsForCompanies() {

	    // The query returns List<Object[]> where each Object[] contains [companyName, location]
	    List<Object[]> companyData = companiesRepository.findCompanyNamesAndLocations();

	    return companyData.stream().map(obj -> {
	        String companyName = (String) obj[0];
	        String locationName = (String) obj[1];

	        String location;

	        if (locationName != null && !locationName.isEmpty() && locationName.matches("[a-zA-Z]+")) {
	            location = locationName;
	        } else {
	            location = "irving";  // default location
	        }

	        // Prepare API variables
	        Map<String, String> uriVariables = Map.of(
	                "location", location,
	                "apiKey", API_KEY
	        );

	        // Call OpenWeatherMap API
	        ResponseEntity<Map> responseEntity = restTemplate.getForEntity(API_URL, Map.class, uriVariables);

	        int statusCode = responseEntity.getStatusCodeValue();

	        if (statusCode == 200) {
	            logger.info("Successfully fetched weather data for location {}: {}", location, responseEntity.getBody());
	        } else {
	            logger.error("Failed to fetch weather data for location {}. Status code: {}", location, statusCode);
	        }

	        // Extract temperature and icon
	        Double temperature = null;
	        String tempIcon = "";

	        if (statusCode == 200 && responseEntity.getBody() != null) {
	            Map<String, Object> weatherResponse = responseEntity.getBody();

	            if (weatherResponse.containsKey("main")) {
	                Map<String, Object> mainData = (Map<String, Object>) weatherResponse.get("main");
	                if (mainData.containsKey("temp")) {
	                    temperature = (Double) mainData.get("temp");
	                }
	            }

	            if (weatherResponse.containsKey("weather")) {
	                List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherResponse.get("weather");

	                if (weatherList != null && !weatherList.isEmpty()) {
	                    Map<String, Object> firstWeather = weatherList.get(0);
	                    if (firstWeather.containsKey("icon")) {
	                        tempIcon = (String) firstWeather.get("icon");
	                    }
	                }
	            }
	        }

	        String tempData = temperature != null ? String.valueOf(temperature) : "No data";

	        // Create and return a new WeatherReport object
	        return new WeatherReport(companyName, tempData, tempIcon);

	    }).collect(Collectors.toList());
	}

	public List<String> getWeatherDetailsForCompaniesByLocation(String locationName) {
		String location;
		if (locationName != null && !locationName.isEmpty() && locationName.matches("[a-zA-Z]+")) {
			location = locationName;

		} else {
			location = "irving";
		}

		// Create a parameter map for the API call
		Map<String, String> uriVariables = Map.of("location", location, // Use dynamic location from company data
				"apiKey", API_KEY);

		// Make the API call and capture the response as ResponseEntity
		ResponseEntity<Map> responseEntity = restTemplate.getForEntity(API_URL, Map.class, uriVariables);

		// Get the status code of the response
		int statusCode = responseEntity.getStatusCodeValue();

		// Log the status code and the response body
		if (statusCode == 200) {
			logger.info("Successfully fetched weather data for location {}: {}", location, responseEntity.getBody());
		} else {
			logger.error("Failed to fetch weather data for location {}. Status code: {}", location, statusCode);
		}

		Double temperature = null;
		String tempIcon = "";
		if (statusCode == 200 && responseEntity.getBody() != null) {
			Map<String, Object> weatherResponse = responseEntity.getBody();
			if (weatherResponse.containsKey("main")) {
				Map<String, Object> mainData = (Map<String, Object>) weatherResponse.get("main");
				if (mainData.containsKey("temp")) {
					temperature = (Double) mainData.get("temp");
				}
			}
			if (weatherResponse.containsKey("weather")) {
				List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherResponse.get("weather");

				if (weatherList != null && !weatherList.isEmpty()) {
					Map<String, Object> firstWeather = weatherList.get(0);
					if (firstWeather.containsKey("icon")) {
						tempIcon = (String) firstWeather.get("icon");
					}
				}
			}
		}

		String myString = temperature != null ? String.valueOf(temperature) : "No data";
		
		List<String> finaldata = new ArrayList<>();
		finaldata.add(myString);
		finaldata.add(tempIcon);
		
		return finaldata;

	}

}
