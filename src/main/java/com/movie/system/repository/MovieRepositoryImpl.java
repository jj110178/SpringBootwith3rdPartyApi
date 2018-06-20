package com.movie.system.repository;

import com.movie.system.model.Movie;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class MovieRepositoryImpl implements MoviesRepositoryCustom {

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public Movie findMovieByName(String name) {
    Query query = entityManager.createNativeQuery("SELECT * FROM movies as em " +
        "WHERE em.name LIKE ?", Movie.class);
    query.setParameter(1, name );

    return (Movie)query.getSingleResult();
  }
}