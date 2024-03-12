package edu.uoc.epcsd.user.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.client.RestTemplate;

import edu.uoc.epcsd.user.services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final UserDetailsServiceImpl userDetailsService;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.csrf(AbstractHttpConfigurer::disable).authorizeHttpRequests(requests -> requests.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().requestMatchers(new AntPathRequestMatcher("/orders/stockReservedByProduct/**")).permitAll().requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll().requestMatchers(new AntPathRequestMatcher("/users/change-password")).permitAll().requestMatchers(new AntPathRequestMatcher("/users/send-change-password-unsafe-email")).permitAll().requestMatchers(new AntPathRequestMatcher("/users/toAlert")).permitAll().requestMatchers(new AntPathRequestMatcher("/auth/admins")).authenticated().requestMatchers(new AntPathRequestMatcher("/users/create")).permitAll().requestMatchers(new AntPathRequestMatcher("/alerts/**")).authenticated().requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated().requestMatchers(new AntPathRequestMatcher("/users/**")).authenticated().requestMatchers(new AntPathRequestMatcher("/orders/**")).authenticated().anyRequest().denyAll()).sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).authenticationProvider(authenticationProvider()).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	    return http.build();
	}

//	.requestMatchers(new AntPathRequestMatcher("/users/create")).denyAll()
//    .requestMatchers(new AntPathRequestMatcher("/users/**")).permitAll()
//    .requestMatchers(new AntPathRequestMatcher("/ZZZ")).authenticated()


	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
