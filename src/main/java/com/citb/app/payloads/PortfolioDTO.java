package com.citb.app.payloads;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PortfolioDTO {

	private int id;
	
	@NotEmpty
	private String title;
	
	@NotEmpty
	private String description;
	
}
