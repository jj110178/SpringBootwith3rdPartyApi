package com.movie.system.repository;

import com.movie.system.model.Movie;

public interface MoviesRepositoryCustom {
    public Movie findMovieByName(String name);
}
