package com.citb.app.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class JwtRequest {
	
	//we could name it email too but since we are using email as username
    private String username;
    private String password;
}
