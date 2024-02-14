package com.devsuperior.dsmovie.dto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.devsuperior.dsmovie.entities.ScoreEntity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ScoreDTO {
	
	private static final DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	@NotNull(message = "Campo requerido")
	private Long movieId;

	@NotNull(message = "Campo requerido")
	@Min(value = 0, message = "Valor mínimo 0")
	@Max(value = 5, message = "Valor máximo 5")
	private Double score;

	public ScoreDTO(Long movieId, Double score) {
		this.movieId = movieId;
		this.score = Double.valueOf(df.format(score));
	}
	
	public ScoreDTO(ScoreEntity score) {
		this.movieId = score.getId().getMovie().getId();
		this.score = Double.valueOf(df.format(score.getValue()));
	}
	
	public Long getMovieId() {
		return movieId;
	}

	public Double getScore() {
		return score;
	}
}
