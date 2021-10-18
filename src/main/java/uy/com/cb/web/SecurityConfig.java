package uy.com.cb.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	/**
	 * bcrypt authentication
	 */
	/*@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		build.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}*/
	
	/**
	 * memory authentication
	 */
	@Override
	 protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 auth.inMemoryAuthentication()
			 .withUser("admin")
				 .password("{noop}123")
				 .roles("ADMIN","USER")
			.and()
			.withUser("user")
				.password("{noop}123")
				.roles("USER");
	 }
	 
	/**
	 * authorization
	 */
	/*@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/editar/**","/agregar/**","/eliminar")
					.hasRole("ADMIN")
				.antMatchers("/")
					.hasAnyRole("USER","ADMIN")
				.and()
					.formLogin()
					.loginPage("/login")
				.and()
					.exceptionHandling().accessDeniedPage("/errores/403");
	}*/
}
