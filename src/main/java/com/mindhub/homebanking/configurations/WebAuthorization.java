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
                //ALL
                .antMatchers(HttpMethod.GET,"/web/index.html", "/web/index.js", "/web/indexStyle.css").permitAll()
                .antMatchers(HttpMethod.GET,"/web/assets/**").permitAll()
                .antMatchers(HttpMethod.POST,"/web/index.html").permitAll()
                .antMatchers(HttpMethod.POST,"/api/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/logout").permitAll()

                //ADMIN
                .antMatchers(HttpMethod.GET,"/rest/**","/h2-console").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/admin/manager.html", "/admin/manager.js").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/admin/loan-application-manager.js", "/admin/loan-application-manager.html").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.POST,"api/loans/manager").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/web/accounts.html", "/web/accounts.js", "/web/accountsstyle.css").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/clients").hasAuthority("ADMIN")

                //CLIENTS
                .antMatchers(HttpMethod.GET,"/web/create-cards.html", "/web/create-cards.js", "/web/create-cards.css").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/cards.html", "/web/cardStyle.css", "/web/cards.js").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/accounts.html", "/web/accounts.js", "/web/accountsstyle.css").hasAnyAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/account.js", "/web/account.html", "/web/accountStyle.css").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/transfer.js", "/web/transfer.html", "/web/transferStyle.css").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/web/loan-application.js", "/web/loan-application.html", "/web/loan-applicationStyle.css").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.GET,"/api/clients/current/accounts/{id}","/api/cards", "api/clients/current/accounts", "api/clients/current/accounts/{id}","api/clients/{id}", "/api/clients/current", "/api/loans", "/api/transactions").hasAuthority("CLIENT")
                .antMatchers(HttpMethod.POST,"api/clients/current/accounts","/api/clients/current/cards", "api/card/payment", "api/clients", "/api/loans", "api/current/loans", "api/transactions" ).hasAuthority("CLIENT")
                .antMatchers(HttpMethod.PUT,"/api/clients/current/accounts", "api/clients/current/cards").hasAuthority("CLIENT")
                //.anyRequest().denyAll()
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
