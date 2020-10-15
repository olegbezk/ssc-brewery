package guru.sfg.brewery.config;

import guru.sfg.brewery.security.RestHeaderAuthFilter;
import guru.sfg.brewery.security.RestUrlAuthFilter;
import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import guru.sfg.brewery.services.JpaUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private JpaUserDetailsService jpaUserDetailsService;

    public RestHeaderAuthFilter restHeaderAuthFilter(AuthenticationManager authenticationManager) {
        RestHeaderAuthFilter filter = new RestHeaderAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    public RestUrlAuthFilter restUrlAuthFilter(AuthenticationManager authenticationManager) {
        RestUrlAuthFilter filter = new RestUrlAuthFilter(new AntPathRequestMatcher("/api/**"));
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(restHeaderAuthFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                .csrf().disable();
        http
                .addFilterBefore(restUrlAuthFilter(authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class);
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/h2-console/**").permitAll() // do not use in production!
                            .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                            .antMatchers(HttpMethod.GET, "/api/v1/beer/**").permitAll()
                            .antMatchers(HttpMethod.DELETE, "/api/v1/beer/**").hasRole("ADMIN")
                            .antMatchers(HttpMethod.GET, "/brewery/api/v1/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                            .antMatchers("/brewery/breweries").hasAnyRole("ADMIN", "CUSTOMER")
                            .antMatchers("/beers/find", "/beers/**").hasAnyRole("ADMIN", "USER", "CUSTOMER")
                            .mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll();
                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().and()
                .httpBasic();
        // H2 console config
        http.headers().frameOptions().sameOrigin();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(this.jpaUserDetailsService).passwordEncoder(passwordEncoder());
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("{bcrypt}$2a$10$3M7Csc634m6OW/D83Ph5hukbGjZJ5TYsDJghxQsWS9z9mJc90ZxHC").roles("ADMIN")
//                .and()
//                .withUser("user").password("{sha256}d294526b7a86705137b3b818f0de4535c0e31a0381d455a31c7c02c95987589cd5191879826860a3").roles("USER")
//                .and()
//                .withUser("scott").password("{bcrypt15}$2a$15$sd0TcL8ZL652qnPebE0f3uVVFQ3L2wLh1/oECv2yx6UaDoN5caNyC").roles("CUSTOMER");
//    }

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("password")
//                .roles("ADMIN")
//                .build();
//
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("password")
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }
}
