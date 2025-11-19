package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ScoreRepository scoreRepository;

    @Mock
    private UserService userService;

    private Long existingMovieId, noExistingMovieId;
    private ScoreEntity score;
    private ScoreDTO scoreDTO;
    private MovieEntity movie;
    private UserEntity user;

    @BeforeEach
    void setUp() throws Exception {
        existingMovieId = 1L;
        noExistingMovieId = 121L;
        score = ScoreFactory.createScoreEntity();
        scoreDTO = ScoreFactory.createScoreDTO();
        movie = MovieFactory.createMovieEntity();
        user =UserFactory.createUserEntity();

        // Configurar o score para estar associado ao movie do teste
        score.setMovie(movie);
        score.setUser(user);
        // Garantir que o score esteja na lista de scores do movie para cobrir o loop
        movie.getScores().add(score);
        
        // loadUserByUsername
        Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
        Mockito.when(movieRepository.findById(noExistingMovieId)).thenReturn(Optional.empty());
        Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(score);
        Mockito.when(movieRepository.save(any())).thenReturn(movie);
    }

	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
        Mockito.when(userService.authenticated()).thenReturn(user);
        
        // Act
        MovieDTO result = service.saveScore(scoreDTO);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(movie.getId(), result.getId());
        Assertions.assertEquals(movie.getTitle(), result.getTitle());
        // Verificar que o score foi calculado corretamente
        Assertions.assertNotNull(result.getScore());
        Assertions.assertTrue(result.getCount() > 0);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
        Mockito.when(userService.authenticated()).thenReturn(user);
        ScoreDTO invalidScoreDTO = new ScoreDTO(noExistingMovieId, 4.5);
        
        // Act & Assert
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.saveScore(invalidScoreDTO);
        });
	}
}
