package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

    @Mock
    private MovieRepository movieRepository;

    private Long existingMovieId, noExistingMovieId, depedentMovieId;
    private MovieEntity movie;
    private MovieDTO movieDTO;
    private PageImpl<MovieEntity> page;

    @BeforeEach
    void setUp() throws Exception {
        existingMovieId = 1L;
        noExistingMovieId = 100L;
        depedentMovieId = 2L;
        movie = MovieFactory.createMovieEntity();
        movieDTO = MovieFactory.createMovieDTO();
        page = new PageImpl<>(List.of(movie));

        // findAll
        Mockito.when(movieRepository.searchByTitle(any(), (Pageable) any())).thenReturn(page);
        // findById
        Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(noExistingMovieId)).thenReturn(Optional.empty());
        // insert
        Mockito.when(movieRepository.save(any())).thenReturn(movie);
        // update
        Mockito.when(movieRepository.getReferenceById(existingMovieId)).thenReturn(movie);
        Mockito.when(movieRepository.getReferenceById(noExistingMovieId)).thenThrow(EntityNotFoundException.class);
        // delete
        Mockito.when(movieRepository.existsById(existingMovieId)).thenReturn(true);
        Mockito.when(movieRepository.existsById(depedentMovieId)).thenReturn(true);
        Mockito.when(movieRepository.existsById(noExistingMovieId)).thenReturn(false);
        Mockito.doNothing().when(movieRepository).deleteById(existingMovieId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(movieRepository).deleteById(depedentMovieId);
    }
	
	@Test
	public void findAllShouldReturnPagedMovieDTO() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<MovieDTO> result = service.findAll("Test Movie", pageable);
        Assertions.assertNotNull(result);
        Assertions.assertEquals("Test Movie", result.iterator().next().getTitle());
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
        MovieDTO result = service.findById(existingMovieId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingMovieId, result.getId());
        Mockito.verify(movieRepository).findById(existingMovieId);
	}

	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            MovieDTO result = service.findById(noExistingMovieId);
            Assertions.assertNull(result);
            Mockito.verify(movieRepository).findById(noExistingMovieId);
        });
	}

	@Test
	public void insertShouldReturnMovieDTO() {
        MovieDTO result = service.insert(movieDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingMovieId, result.getId());
	}

	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
        MovieDTO result = service.update(existingMovieId, movieDTO);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(existingMovieId, result.getId());
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            MovieDTO result = service.update(noExistingMovieId, movieDTO);
            Assertions.assertNull(result);
        });
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
        Assertions.assertDoesNotThrow(() -> {
            service.delete(existingMovieId);
            Mockito.verify(movieRepository).deleteById(existingMovieId);
        });
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(noExistingMovieId);
            Mockito.verify(movieRepository).deleteById(noExistingMovieId);
        });
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
        Assertions.assertThrows(DatabaseException.class, () -> {
            service.delete(depedentMovieId);
            Mockito.verify(movieRepository).deleteById(depedentMovieId);
        });
	}
}
