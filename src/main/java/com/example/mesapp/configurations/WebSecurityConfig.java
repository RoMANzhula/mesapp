package com.example.mesapp.configurations;


import com.example.mesapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) //для дозволу мати привілегійовані ролі (такі як адмін наприклад)
public class WebSecurityConfig {

    @Autowired
    private UserService userService;

    @Autowired
    private Encoders passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http
                .authorizeHttpRequests((requests) ->
                        requests
                                .requestMatchers("/", "/registration", "/static/**", "/activate/*").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout((logout) ->
                        logout.permitAll()
                )
                .securityContext((securityContext) -> securityContext
                        .requireExplicitSave(true)
                )
                .rememberMe((remember) -> remember
                        .rememberMeServices(rememberMeServices)
                )
        ;

        return http.build();
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
        TokenBasedRememberMeServices.RememberMeTokenAlgorithm encodingAlgorithm = TokenBasedRememberMeServices.RememberMeTokenAlgorithm.SHA256;
        TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices(passwordEncoder.toString(), userDetailsService, encodingAlgorithm);
        rememberMe.setMatchingAlgorithm(TokenBasedRememberMeServices.RememberMeTokenAlgorithm.MD5);
        return rememberMe;
    }


    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userService) //отримуємо інфо про користувача з БД
//                .passwordEncoder(NoOpPasswordEncoder.getInstance()) // Не використовуйте NoOpPasswordEncoder у продакшені
                .passwordEncoder(passwordEncoder.userPasswordEncoder())
        ;
    }
}


//      КОД ДЛЯ РЕЄСТРАЦІЇ ТА АВТОРИЗАЦІЇ КОРИСТУВАЧІВ з використанням jdbcAuthentication()
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.crypto.password.NoOpPasswordEncoder;
//import org.springframework.security.provisioning.JdbcUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import javax.sql.DataSource;
//
//@Configuration
//public class WebSecurityConfig {
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests((authorizeRequests) ->
//                        authorizeRequests
//                                .requestMatchers("/", "/registration").permitAll()
//                                .anyRequest().authenticated()
//                )
//                .formLogin((formLogin) ->
//                        formLogin
//                                .loginPage("/login")
//                                .permitAll()
//                )
//                .logout((logout) ->
//                        logout.permitAll()
//                )
//                .csrf((csrf) ->
//                        csrf.disable()
//                )
//        ;
//
//        return http.build();
//    }
//
//    @Bean
//    public JdbcUserDetailsManager userDetailsManager() {
//        return new JdbcUserDetailsManager(dataSource);
//    }
//
//
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .jdbcAuthentication()
//                .dataSource(dataSource)
//                .passwordEncoder(NoOpPasswordEncoder.getInstance()) // Не використовуйте NoOpPasswordEncoder у продакшені
//                .usersByUsernameQuery("SELECT username, password, active FROM usr WHERE username=?")
//                .authoritiesByUsernameQuery("SELECT u.username, ur.roles FROM usr u INNER JOIN user_role ur ON u.id = ur.user_id WHERE u.username=?");
//    }
//
//  //для створення користувача
//	@Bean
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//			 User.withDefaultPasswordEncoder()
//				.username("user")
//				.password("password")
//				.roles("USER")
//				.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
//}
