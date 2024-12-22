package com.citb.app.payloads;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JwtResponse {

	
	private String jwtToken;
	private String username;
}
