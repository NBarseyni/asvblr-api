package com.pa.asvblrapi.config;

import com.pa.asvblrapi.jwt.AuthEntryPointJwt;
import com.pa.asvblrapi.jwt.AuthTokenFilter;
import com.pa.asvblrapi.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticationFiler() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                // Article
                .antMatchers(HttpMethod.GET, "/api/articles/{id}", "/api/articles/list").hasAuthority("ARTICLE_MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/articles/**").hasAuthority("ARTICLE_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/articles/**").hasAuthority("ARTICLE_MANAGEMENT")
                .antMatchers(HttpMethod.PATCH, "/api/articles/**").hasAuthority("ARTICLE_MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/articles/**").hasAuthority("ARTICLE_MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/articles").permitAll()
                // Auth
                .antMatchers("/api/auth/signup").hasAuthority("SIGNUP")
                .antMatchers("/api/auth/signin", "/api/auth/change-password", "/api/auth/reset-password",
                        "/api/auth/save-password", "/api/auth/update-password").permitAll()
                // Matches
                .antMatchers("/api/matches/**").hasAuthority("MATCH_MANAGEMENT")
                // Players
                .antMatchers(HttpMethod.GET, "/api/players").hasAuthority("PLAYER_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/players/**").hasAuthority("PLAYER_MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/players/**").hasAuthority("PLAYER_MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/players/{id}").hasAuthority("PLAYER_READ")
                // Prices
                .antMatchers(HttpMethod.PATCH, "/api/prices/**").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/prices/**").permitAll()
                // Roles && getUserById
                .antMatchers("/api/roles/**").hasAuthority("USER_MANAGEMENT")
                .antMatchers(HttpMethod.GET,"/api/users/{id}").hasAuthority("USER_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("USER_MANAGEMENT")
                .antMatchers(HttpMethod.PATCH, "/api/users/**").hasAuthority("USER_MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/users/{id}").hasAuthority("USER_MANAGEMENT")
                // Users
                .antMatchers(HttpMethod.GET, "/api/users").hasAuthority("USER_READ")
                .antMatchers(HttpMethod.GET, "/api/users/{id}/player}", "/api/users/{id}/driving-drives",
                        "/api/users/{id}/passenger-drives").permitAll()
                // Season
                .antMatchers(HttpMethod.POST,"/api/seasons/**").hasAuthority("SEASON_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/seasons/**").hasAuthority("SEASON_MANAGEMENT")
                .antMatchers(HttpMethod.GET, "/api/seasons/current-season").permitAll()
                // Statistics
                .antMatchers("/api/statistics/**").hasAuthority("STATISTICS_READ")
                // Subscriptions
                .antMatchers(HttpMethod.GET, "/api/subscriptions").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/subscriptions/{id}/cni", "/api/subscriptions/{id}/identity-photo",
                        "/api/subscriptions/{id}/medical-certificate").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/subscriptions/**").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/subscriptions/**").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.PATCH, "/api/subscriptions/**").hasAuthority("SUBSCRIPTION_MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/subscriptions", "/api/subscriptions/{id}/documents").permitAll()
                // Teams
                .antMatchers(HttpMethod.GET, "/api/teams/list-detail").hasAuthority("TEAM_MANAGEMENT")
                .antMatchers(HttpMethod.PUT, "/api/teams/{id}").hasAuthority("TEAM_MANAGEMENT")
                .antMatchers(HttpMethod.DELETE, "/api/teams/**").hasAuthority("TEAM_MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/teams", "/api/teams/{id}/coach", "/api/teams/{id}/photo",
                        "/api/teams/{id}/players").hasAuthority("TEAM_MANAGEMENT")
                .antMatchers(HttpMethod.POST, "/api/teams/{id}/leader").hasAuthority("TEAM_MANAGEMENT_COACH")
                .antMatchers(HttpMethod.PUT, "/api/teams/{idTeam}/players/{idPlayer}").hasAuthority("TEAM_MANAGEMENT_COACH")
                .antMatchers(HttpMethod.GET, "/api/teams", "/api/teams/{id}", "/api/teams/{id}/players",
                        "/api/teams/{id}/matches").permitAll()
                // Drives
                .antMatchers("/api/drives/**").hasAuthority("DRIVE_READ_CREATE")
                // Dataset
                .antMatchers("/api/clothing-sizes/**", "/api/payment-modes/**", "/api/positions/**",
                        "/api/subscription-categories/**", "/api/team-categories/**").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationFiler(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**",
                "/photos/**");
    }

    @Bean
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(100000000);
        return multipartResolver;
    }
}
