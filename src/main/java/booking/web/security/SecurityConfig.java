package booking.web.security;

import booking.web.controller.BookingController;
import booking.web.controller.UserController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

@EnableWebSecurity
@Import(UserDaoUserDetailsService.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDaoUserDetailsService userDaoUserDetailsService;

    @Autowired
    public SecurityConfig(UserDaoUserDetailsService userDaoUserDetailsService) {
        this.userDaoUserDetailsService = userDaoUserDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(BookingController.TICKETS_ENDPOINT).hasRole(Roles.BOOKING_MANAGER)
                .antMatchers(UserController.REGISTER_ENDPOINT).permitAll()
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

    //TODO PasswordEncoder#matches is not used, users registered via UI can't login
    @Bean
    PasswordEncoder passwordEncoder() {
        return new StandardPasswordEncoder(); // Salt is added automatically
    }

    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDaoUserDetailsService);
        return provider;
    }

}