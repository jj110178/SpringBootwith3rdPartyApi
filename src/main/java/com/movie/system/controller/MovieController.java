package com.movie.system.controller;

import com.movie.system.model.Body;
import com.movie.system.model.Details;
import com.movie.system.model.Movie;
import com.movie.system.model.ResourceNotFoundException;
import com.movie.system.repository.MovieRepository;
import com.movie.system.service.MoviesService;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class MovieController {

  @Autowired
  MoviesService service;

  @Autowired
  MovieRepository movieRepo;

  @Value("${serviceURL}")
  String serviceURL;

  // Get All Movies
  @GetMapping("/movies")
  public List<Movie> getAllMovies() {
    return movieRepo.findAll();
  }

  // create a new Movie
  @PostMapping("/movies")
  public Movie createNewMovie(@Valid @RequestBody Movie movie){
    return movieRepo.save(movie);
  }

  // get a single Movie by Id
  @GetMapping("/movies/{id}")
  public Movie getMovieById(@PathVariable(value = "id") Long id) {
    Movie nmovie = new Movie();
    nmovie = movieRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie", "Movie Id", id));
    return service.buildMoviewithDetails(serviceURL, nmovie);
  }

  // get a single Movie by Name
  @GetMapping(value = "/movies", params = "name", produces = "application/json")
  public Movie getMovieByName(@RequestParam(value = "name") String name) throws IOException {
    Movie nmovie = new Movie();
    nmovie = movieRepo.findMovieByName(name);
    return service.buildMoviewithDetails(serviceURL, nmovie);
  }

  // Update Movie
  @PutMapping("/movies/{id}")
  public Movie updateMovie(@PathVariable(value = "id") Long id, @Valid @RequestBody Movie movie){
    Movie updateMovie = movieRepo
        .findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie", "Movie Id", id));
    updateMovie.setName(movie.getName());
    return movieRepo.save(updateMovie);
  }

  // Delete Movie
  @DeleteMapping("/movies/{id}")
  public ResponseEntity<?> deleteMovie(@PathVariable(value = "id") Long id) {
    Movie movie = movieRepo.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Movie", "Movie Id", id));

    movieRepo.delete(movie);

    return ResponseEntity.ok().build();
  }
}
