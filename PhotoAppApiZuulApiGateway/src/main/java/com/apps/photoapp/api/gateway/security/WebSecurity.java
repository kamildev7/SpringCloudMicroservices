package com.apps.photoapp.api.gateway.security;

        import org.springframework.context.annotation.Configuration;
        import org.springframework.core.env.Environment;
        import org.springframework.http.HttpMethod;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
        import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
        import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final String API_USERS_ACTUATOR_URL_PATH = "api.users.actuator.url.path";
    private static final String API_ZUUL_ACTUATOR_URL_PATH = "api.zuul.actuator.url.path";
    private static final String API_H_2_CONSOLE_URL_PATH = "api.h2console.url.path";
    private static final String API_REGISTRATION_URL_PATH = "api.registration.url.path";
    private static final String API_LOGIN_URL_PATH = "api.login.url.path";
    private final Environment environment;

    public WebSecurity(final Environment environment) {
        this.environment = environment;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().frameOptions().disable();

        http.authorizeRequests().antMatchers(environment.getProperty(API_USERS_ACTUATOR_URL_PATH)).permitAll()
                .antMatchers(environment.getProperty(API_ZUUL_ACTUATOR_URL_PATH)).permitAll()
                .antMatchers(environment.getProperty(API_H_2_CONSOLE_URL_PATH)).permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty(API_REGISTRATION_URL_PATH)).permitAll()
                .antMatchers(HttpMethod.POST, environment.getProperty(API_LOGIN_URL_PATH)).permitAll().anyRequest()
                .authenticated().and().addFilter(new AuthorizationFilter(authenticationManager(), environment));

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}
