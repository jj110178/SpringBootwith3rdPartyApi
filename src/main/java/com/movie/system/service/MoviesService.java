package com.movie.system.service;

import com.movie.system.model.Body;
import com.movie.system.model.Details;
import com.movie.system.model.Movie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MoviesService {

  public Movie buildMoviewithDetails(String uri, Movie movie) {
    String url = uri+movie.getName();
    Movie nMovie = new Movie();
    nMovie.setId(movie.getId());
    nMovie.setCreatedAt(movie.getCreatedAt());
    nMovie.setName(movie.getName());

    Details details = new Details();
    RestTemplate restTemp = new RestTemplate();
    ResponseEntity<Body[]> responseEntity = restTemp.getForEntity(url, Body[].class);
    details.setSource(url);
    details.setBody(responseEntity.getBody());
    nMovie.setDetails(details);
    return nMovie;
  }

}
