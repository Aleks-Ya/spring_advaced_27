package booking.web.security;

import booking.web.controller.AccountController;
import booking.web.controller.BookingController;
import booking.web.controller.UserController;
import booking.web.rest.RestConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Import({UserDaoUserDetailsService.class, PasswordEncoderConfig.class})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDaoUserDetailsService userDaoUserDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserDaoUserDetailsService userDaoUserDetailsService, PasswordEncoder passwordEncoder) {
        this.userDaoUserDetailsService = userDaoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(BookingController.SHOW_ALL_TICKETS_ENDPOINT).hasRole(Roles.BOOKING_MANAGER)
                .antMatchers(BookingController.SHOW_TICKETS_BY_EVENT_ENDPOINT).hasRole(Roles.BOOKING_MANAGER)
                .antMatchers(UserController.SHOW_ALL_USERS_ENDPOINT).hasRole(Roles.BOOKING_MANAGER)
                .antMatchers(AccountController.ROOT_ENDPOINT + "/**").hasRole(Roles.BOOKING_MANAGER)
                .antMatchers(UserController.REGISTER_ENDPOINT, RestConfig.REST_ROOT_ENDPOINT + "/**").permitAll()
                .anyRequest().authenticated()
                .and()

                .logout().permitAll().and()

                .formLogin().loginPage("/login").permitAll().and()

                .rememberMe().and()

                .exceptionHandling().accessDeniedPage("/access_denied").and()

                .csrf().disable();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDaoUserDetailsService);
    }

    @Bean
    AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDaoUserDetailsService);
        return provider;
    }

}