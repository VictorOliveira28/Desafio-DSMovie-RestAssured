package com.devsuperior.dsmovie.dto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.devsuperior.dsmovie.entities.MovieEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class MovieDTO {

	private static final DecimalFormat df = new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	private Long id;
	
	@NotBlank(message = "Campo requerido")
	@Size(min = 5, max = 80, message = "Tamanho deve ser entre 5 e 80 caracteres")
	private String title;
	
	private Double score;
	
	private Integer count;
	
	private String image;

	public MovieDTO(Long id, String title, Double score, Integer count, String image) {
		this.id = id;
		this.title = title;
		this.score = Double.valueOf(df.format(score));
		this.count = count;
		this.image = image;
	}

	public MovieDTO(MovieEntity movie) {
		this(movie.getId(), movie.getTitle(), movie.getScore(), movie.getCount(), movie.getImage());
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public Double getScore() {
		return score;
	}
	
	public Integer getCount() {
		return count;
	}

	public String getImage() {
		return image;
	}

	@Override
	public String toString() {
		return "MovieDTO [id=" + id + ", title=" + title + ", score=" + score + ", count=" + count + ", image=" + image
				+ "]";
	}
}
