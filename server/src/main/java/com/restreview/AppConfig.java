package com.restreview;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.restreview.etc.AverageScoreCalculator;
import com.restreview.etc.PasswordGenerator;
import com.restreview.etc.RandomPasswordGenerator;
import com.restreview.etc.ScoreCalculator;
import com.restreview.etc.SessionManager;

/*
 * Spring Application Bean Configuration 클래스
 * */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public PasswordGenerator passwordGenerator() {
		return new RandomPasswordGenerator();
	}
	
	@Bean
	public ScoreCalculator scoreCalculator() {
		return new AverageScoreCalculator();
	}
	@Bean
	public SessionManager sessionManager() {
		return new SessionManager();
	}
	
}
