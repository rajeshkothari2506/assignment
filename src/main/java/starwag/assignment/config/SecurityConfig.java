package starwag.assignment.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
    return new MvcRequestMatcher.Builder(introspector);
  }

  @Bean
  public SecurityFilterChain configure(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .headers(httpSecurityHeadersConfigurer -> httpSecurityHeadersConfigurer.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(mvc.pattern("/swagger-ui/**")).permitAll()
            .requestMatchers(mvc.pattern("/error")).permitAll()
            .requestMatchers(mvc.pattern("/v3/**")).permitAll()
            .anyRequest().authenticated())
        .httpBasic(withDefaults());
    return http.build();
  }
}
