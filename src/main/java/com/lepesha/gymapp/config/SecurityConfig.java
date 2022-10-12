package com.lepesha.gymapp.config;

import com.lepesha.gymapp.model.Permission;
import com.lepesha.gymapp.model.Person;
import com.lepesha.gymapp.model.Role;
import com.lepesha.gymapp.model.Status;
import com.lepesha.gymapp.repository.PersonRepository;
import com.lepesha.gymapp.security.JwtConfigurer;
import com.lepesha.gymapp.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Jwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
//@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
                authorizeRequests()
                    .antMatchers("/").permitAll()
                    .antMatchers(HttpMethod.GET, "/gym").hasAuthority(Permission.DEVELOPERS_READ.getPermission())
                    .antMatchers(HttpMethod.GET, "/gym/offers").hasAuthority(Permission.DEVELOPERS_WRITE.getPermission())
                    .antMatchers("/gym/auth/login").permitAll()
                    .antMatchers("/gym/registration").permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                .apply(jwtConfigurer);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    protected PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12);
    }



//    @Bean
//    public PrincipalExtractor principalExtractor(PersonRepository personRepository) {
//
//        return map -> {
//            Integer id = (Integer) map.get("id");
//
//            Person person = personRepository.findById(id).orElseGet(() -> {
//                Person newPerson = new Person();
//
//                newPerson.setId(id);
//                newPerson.setEmail((String) map.get("email"));
//                newPerson.setFirstName((String) map.get("given_name"));
//                newPerson.setLastName((String) map.get("family_name"));
//                newPerson.setAge(99);
////                newPerson.setRole("USER");
////                newPerson.setStatus(Status.valueOf("ACTIVE"));
//                return newPerson;
//            });
//            return personRepository.save(person);
//        };
//    }
}
