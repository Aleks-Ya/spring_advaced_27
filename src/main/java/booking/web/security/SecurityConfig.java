package booking.web.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
        http.formLogin().loginPage("/login").permitAll()
                .and().rememberMe()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().logout().permitAll()
                .and().csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/user");
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDaoUserDetailsService);
    }

}