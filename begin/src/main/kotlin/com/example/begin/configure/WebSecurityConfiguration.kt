package com.example.begin.configure

import com.example.begin.service.UserCustomService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Configurable
import org.springframework.context.annotation.Bean
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.WebSecurityConfigurer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configurable
@EnableWebSecurity
class WebSecurityConfiguration: WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var userCustomService: UserCustomService

    @Bean
    fun encoder(): PasswordEncoder = BCryptPasswordEncoder(11) // 문자열 11 개

    @Bean
    fun authenticationProvider(): DaoAuthenticationProvider {
        val authProvider = DaoAuthenticationProvider()
        authProvider.setUserDetailsService(userCustomService)
        authProvider.setPasswordEncoder(encoder())
        return authProvider
    }

    // 얘는 인증이죠.
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider())
    }

    // 얘는 권한이고요.
    override fun configure(http: HttpSecurity?) {
        http
            ?.csrf()?.disable()
            // h2 localhost 연결 거부에 대한 수정사항.
            ?.headers()?.frameOptions()?.disable()
        ?.and()
            // 인증 요청에 대한 정의
            ?.authorizeRequests()
                // "/resources/**" 아래는 권한 허용.
                ?.antMatchers("/resources/**")?.permitAll()
                // ?.antMatchers("/h2/**")?.permitAll()
                // 나머지 모든 Request 는 권한을 허용하겠다.
                ?.anyRequest()?.permitAll()
                // "/admin/**" 아래의 권한은 ROLE_ADMIN 만 허용한다.
        ?.and()
            // 로그인 페이지에 대한 정보를 입력!
            ?.formLogin()
                // 로그인 페이지의 url
                ?.loginPage("/login")
                // 로그인 페이지의 form의 action의 url
                ?.loginProcessingUrl("/login")
                // 로그인 성공 후의 어디로 이동할 것 인지.
                ?.defaultSuccessUrl("/home")
        ?.and()
            // 로그아웃에 대한 정의.
            ?.logout()
                ?.logoutUrl("/logout")

    }
}