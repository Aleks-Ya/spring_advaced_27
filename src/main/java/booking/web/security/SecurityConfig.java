package booking.web.security;

import booking.web.controller.BookingController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;

/**
 * @author Aleksey Yablokov
 */
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
                .antMatchers(BookingController.ENDPOINT + "/tickets").hasRole(Roles.BOOKING_MANAGER)
                .anyRequest().authenticated()
                .and()

                .logout().permitAll()
                .and()

                .formLogin().loginPage("/login").permitAll().and()

                .rememberMe().and()

                .exceptionHandling().accessDeniedPage("/access_denied").and()

                .csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/user");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDaoUserDetailsService);
    }

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