package com.jwt_auth.config;

import com.jwt_auth.security.JwtAuthenticationEntryPoint;
import com.jwt_auth.security.JwtAuthenticationTokenFilter;
import com.jwt_auth.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    private final JwtAuthenticationEntryPoint unauthorizedHandler;

    private final JwtAuthenticationTokenFilter authenticationTokenFilterBean;

    @Autowired
    public WebSecurityConfig(
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtAuthenticationEntryPoint unauthorizedHandler,
            JwtAuthenticationTokenFilter authenticationTokenFilterBean) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.unauthorizedHandler = unauthorizedHandler;
        this.authenticationTokenFilterBean = authenticationTokenFilterBean;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userService).passwordEncoder(this.passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                // we don't need CSRF because our token is invulnerable
                .csrf().disable()

                // returning 401 when unauthorized
                .exceptionHandling().authenticationEntryPoint(this.unauthorizedHandler).and()

                // don't create session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

                .authorizeRequests()

                // allow anonymous resource requests
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()

                .antMatchers("/register", "/login").permitAll()

                .antMatchers("/users").hasRole("ADMIN")

                .anyRequest().authenticated();

        // JWT based security filter
        httpSecurity
                .addFilterBefore(this.authenticationTokenFilterBean, UsernamePasswordAuthenticationFilter.class);

        // disable page caching
        httpSecurity.headers().cacheControl();
    }
}