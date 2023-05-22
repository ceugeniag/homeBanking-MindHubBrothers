package com.mindhub.homebanking.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@CrossOrigin
@EnableWebSecurity
@Configuration
public class WebAuthorization{
    @Bean
    protected SecurityFilterChain filterchain(HttpSecurity http) throws Exception {
        //Cross-Origin Resource Sharing
        http.cors().and().authorizeRequests()
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests()
                .antMatchers("/web/index.html").permitAll()
                .antMatchers("/web/assets/**").permitAll()
                .antMatchers("/web/index.js").permitAll()
                .antMatchers("/web/indexStyle.css").permitAll()
                .antMatchers(HttpMethod.POST,"/web/index.html").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()
                .antMatchers("/rest/**").hasAuthority("ADMIN")
                .antMatchers("/h2-console").hasAuthority("ADMIN")
                .antMatchers("/admin/manager.html").hasAuthority("ADMIN")
                .antMatchers("/admin/manager.js").hasAuthority("ADMIN")
                .antMatchers("/web/account.html").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/loans").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/loans/manager").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/clients/current/cards").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PUT, "/api/clients/current/accounts").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST, "api/loans/manager").hasAuthority("ADMIN")
                .antMatchers("/web/cards.html").hasAuthority("CLIENT")
                .antMatchers("/web/accounts.html").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/web/accounts.js").hasAnyAuthority("CLIENT","ADMIN")
                .antMatchers("/web/cards.js").hasAuthority("CLIENT")
                .antMatchers("/web/account.js").hasAuthority("CLIENT")
                .antMatchers("/web/transfer.js").hasAuthority("CLIENT")
                .antMatchers("/web/transfer.html").hasAuthority("CLIENT")
                .antMatchers("/web/loan-application.js").hasAuthority("CLIENT")
                .antMatchers("/web/loan-application.html").hasAuthority("CLIENT")
        ;

        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/login");
        http.logout()
                .logoutUrl("/api/logout").deleteCookies("JSESSIONID");

    // turn off checking for CSRF tokens

     http.csrf().disable();

    //disabling frameOptions so h2-console can be accessed
        http.headers().frameOptions().disable();

        // if user is not authenticated, just send an authentication failure response

     http.exceptionHandling().authenticationEntryPoint((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    // if login is successful, just clear the flags asking for authentication

     http.formLogin().successHandler((req, res, auth) -> clearAuthenticationAttributes(req));

    // if login fails, just send an authentication failure response

     http.formLogin().failureHandler((req, res, exc) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED));

    // if logout is successful, just send a success response

     http.logout().logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler());

     return http.build();

}
    private void clearAuthenticationAttributes(HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {

            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);

        }

    }
}
