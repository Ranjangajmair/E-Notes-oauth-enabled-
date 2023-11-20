package com.prog.config;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.prog.entity.Notes;
import com.prog.entity.User;
import com.prog.repository.UserRepository;
import com.prog.service.NotesService;
import com.prog.service.UserService;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepo;

	@Autowired
	UserService userService;
	
	@Autowired
	NotesService notesService;

	@Autowired
	PasswordGenerator generator;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		String redirectUrl = null;
		if (authentication.getPrincipal() instanceof DefaultOAuth2User) {
			DefaultOAuth2User userDetails = (DefaultOAuth2User) authentication.getPrincipal();
			String userEmail = userDetails.getAttribute("email");
			String username = userDetails.getAttribute("name");

			if (userRepo.findByEmail(userEmail) == null) {
				User user = new User();
				
				user.setEmail(userEmail);
				user.setName(username);
				user.setPassword(("generator"));
				user.setRole("USER");
				userService.saveUser(user);
			}

		}
		redirectUrl = "/user/addNotes";
		new DefaultRedirectStrategy().sendRedirect(request, response, redirectUrl);
	}

}