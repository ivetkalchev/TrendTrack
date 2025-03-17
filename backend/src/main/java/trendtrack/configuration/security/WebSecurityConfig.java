package trendtrack.configuration.security;

import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.security.config.http.SessionCreationPolicy;
import static org.springframework.security.config.Customizer.withDefaults;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import trendtrack.configuration.security.auth.AuthenticationRequestFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true)
@Configuration
public class WebSecurityConfig {

    private static final String[] SWAGGER_UI_RESOURCES = {
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/swagger-ui/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity,
                                           AuthenticationEntryPoint authenticationEntryPoint,
                                           AuthenticationRequestFilter authenticationRequestFilter)
            throws Exception {
        httpSecurity
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(configurer ->
                        configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(registry ->
                        registry.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                                //authentication
                                .requestMatchers(HttpMethod.POST, "/tokens/**", "/users").permitAll()
                                .requestMatchers("/register").permitAll()
                                //users
                                .requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/{id}").hasAnyRole("ADMIN", "CLIENT")
                                .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "CLIENT")
                                //fabrics
                                .requestMatchers(HttpMethod.GET, "/fabrics").permitAll()
                                .requestMatchers(HttpMethod.POST, "/fabrics").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/fabrics/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/fabrics/{id}").hasRole("ADMIN")
                                //cart
                                .requestMatchers(HttpMethod.GET, "/cart/{userId}").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.POST, "/cart/{userId}/add").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.PUT, "/cart/{userId}/update").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.DELETE, "/cart/{userId}/remove").hasRole("CLIENT")
                                //orders
                                .requestMatchers(HttpMethod.POST, "/orders").hasRole("CLIENT")
                                .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/orders/{id}").hasAnyRole("CLIENT", "ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/orders").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/orders/{id}").hasRole("ADMIN")
                                .requestMatchers(HttpMethod.GET, "/orders/users/{userId}").hasRole("CLIENT")
                                //websocket
                                .requestMatchers("/ws/**").permitAll()

                                .requestMatchers(SWAGGER_UI_RESOURCES).permitAll()
                                .anyRequest().authenticated()
                )
                .exceptionHandling(configure -> configure.authenticationEntryPoint(authenticationEntryPoint))
                .addFilterBefore(authenticationRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Authorization")
                        .allowCredentials(true);
            }
        };
    }
}