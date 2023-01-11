package com.zahir.fathurrahman.springsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthFilter.setFilterProcessesUrl("/api/login");

        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // URUTAN AUTHORIZE REQUEST DIDAHULUKAN YANG LOSS DULU yang pake permit all baru sisanya yang di protected
        // contoh kek dibawah ini
        // http.authorizeRequests().antMatchers(HttpMethod.GET,"/product").permitAll();

        // pake ini biar kalo baris 33 ganti url ga diblok
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/login").permitAll();
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/refresh_token").permitAll();

        // artinya untuk request method get yang endpointnyaa api/users/ dan prefix apapun dapat di akses oleh role user
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/users/**").hasAnyAuthority("ROLE_USER");

        // setting semua request harus berautentikasi
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }
}
