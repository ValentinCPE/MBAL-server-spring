package com.worldgether.mbal;

import com.worldgether.mbal.Security.CustomUserDetails;
import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.Role;
import com.worldgether.mbal.model.User;
import com.worldgether.mbal.repository.FamilyRepository;
import com.worldgether.mbal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

@EnableResourceServer
@SpringBootApplication(scanBasePackages = {"com.worldgether.mbal"})
public class MbalApplication extends SpringBootServletInitializer {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private FamilyRepository familyRepository;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(MbalApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(MbalApplication.class, args);
	}

	/**
	 * Password grants are switched on by injecting an AuthenticationManager.
	 * Here, we setup the builder so that the userDetailsService is the one we coded.
	 * @param builder
	 * @param repository
	 * @throws Exception
	 */
	@Autowired
	public void authenticationManager(AuthenticationManagerBuilder builder, UserRepository repository) throws Exception {

		//Setup a default user if db is empty
		if (repository.count()==0) {
			Family family = new Family();
			family.setName("Guevara");
			family.setPassword(passwordEncoder.encode("Valentin34"));
			family.setCreation_date(new Timestamp(new Date().getTime()));
			familyRepository.save(family);

			User user = new User();
			user.setNom("Admin");
			user.setPrenom("Admin");
			user.setMail("admin");
			user.setPassword(passwordEncoder.encode("Valentin34"));
			user.setCreation_date(new Timestamp(new Date().getTime()));
			user.setNumero_telephone("0000000000");
			user.setProfile_picture_path("testPhoto.png");
			user.setFamily(family);
			user.setRoles(Arrays.asList(new Role("USER"), new Role("ADMIN")));
			repository.save(user);
		}

		if(repository.findByMail("testAmazon") == null){
			Family family = new Family();
			family.setName("AmazonFamily");
			family.setPassword(passwordEncoder.encode("amazon"));
			family.setCreation_date(new Timestamp(new Date().getTime()));
			familyRepository.save(family);

			User user = User.builder()
				.nom("Test")
				.prenom("Amazon")
				.mail("testAmazon")
				.password(passwordEncoder.encode("Valentin34"))
				.creation_date(new Timestamp(new Date().getTime()))
				.numero_telephone("0000000000")
				.family(family)
				.roles(Arrays.asList(new Role("USER"), new Role("AMAZON")))
				.build();
			repository.save(user);
		}

		builder.userDetailsService(userDetailsService(repository)).passwordEncoder(passwordEncoder);
	}

	/**
	 * We return an instance of our CustomUserDetails.
	 * @param repository
	 * @return
	 */
	private UserDetailsService userDetailsService(final UserRepository repository) {
		return username -> new CustomUserDetails(repository.findByMail(username));
	}

}
