package com.opengalk.server.配置类;

import com.opengalk.server.过滤器.JavaWebTokenFilter;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;

@Slf4j
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JavaWebTokenFilter javaWebTokenFilter;

    private final AccessDeniedHandler accessDeniedHandler;

    private final AuthenticationFailureHandler authenticationFailureHandler;

    private final AuthenticationSuccessHandler authenticationSuccessHandler;

    private final LogoutSuccessHandler logoutSuccessHandler;

    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(@NotNull AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(@NotNull HttpSecurity http) throws Exception {
        return http
                // 跨域配置
                .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource))
                // 不使用csrf防护
                .csrf(AbstractHttpConfigurer::disable)
                // 登陆
                .formLogin(
                        formLoginCustomizer -> formLoginCustomizer
                                .loginProcessingUrl("/login")
                                .usernameParameter("account")
                                .passwordParameter("password")
                                .failureHandler(authenticationFailureHandler)
                                .successHandler(authenticationSuccessHandler))
                // 注销
                .logout(logoutCustomizer -> logoutCustomizer
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(logoutSuccessHandler))
                // 不通过Session获取SecurityContext
                .sessionManagement(
                        sessionManagementCustomizer -> sessionManagementCustomizer
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        authorizeHttpRequestsCustomizer -> authorizeHttpRequestsCustomizer
                                // 对于登录、注册和验证码接口，允许匿名访问
                                .requestMatchers("/login").anonymous()
                                .requestMatchers("/register").anonymous()
                                .requestMatchers("/verify/**").anonymous()
                                // 除上面的请求外，其他所有请求全部需要鉴权认证
                                .anyRequest().authenticated())
                .exceptionHandling(
                        exceptionHandlingCustomizer -> exceptionHandlingCustomizer
                                .accessDeniedHandler(accessDeniedHandler))
                // jwt过滤
                .addFilterBefore(javaWebTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
