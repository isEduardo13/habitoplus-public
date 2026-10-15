package com.habitoplus.habitoplusback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
@SpringBootApplication
@EnableScheduling
public class HabitoplusbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitoplusbackApplication.class, args);
	}
}
