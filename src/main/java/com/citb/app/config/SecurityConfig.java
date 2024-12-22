package com.citb.app.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties.Admin;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.citb.app.security.CustomUserDetailService;
import com.citb.app.security.JwtAuthenticationEntryPoint;
import com.citb.app.security.JwtAuthenticationFilter;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;



@Configuration
@EnableWebSecurity
@EnableWebMvc
@EnableMethodSecurity

public class SecurityConfig  {


	@Autowired
	private CustomUserDetailService customUserDetailService;
    @Autowired
    private JwtAuthenticationFilter filter;
    
    @Autowired
    private JwtAuthenticationEntryPoint entryPoint;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			// ...
			.csrf(csrf -> csrf.disable())
			.cors(cors -> cors.disable())
			.authorizeHttpRequests(auth -> auth
					.requestMatchers("/api/v1/portfolios/**", "/api/v1/users/**", "/api/v1/meetings/**" ).authenticated()
					.requestMatchers("/v3/**").permitAll()
					.anyRequest().permitAll()
					)
					
			
			
        	.exceptionHandling(ex -> ex.authenticationEntryPoint(entryPoint))
		    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		
		
		http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);
			
		return http.build();
	}
	
	@Bean 
	public DaoAuthenticationProvider doDaoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		daoAuthenticationProvider.setUserDetailsService(customUserDetailService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
	}

    @Bean
    PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }
    
    @Bean
    public GroupedOpenApi customApi() {
    	
    	return  GroupedOpenApi.builder().group("api-group").pathsToMatch("/**").build();
    }

 
    
    //changing the title and other texts in the swagger html document
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Citb app")
                .description("This project is for learning")
                .version("v0.0.1")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                .description("SpringShop Wiki Documentation")
                .url("https://springshop.wiki.github.org/docs"));
    }
  
     
}

