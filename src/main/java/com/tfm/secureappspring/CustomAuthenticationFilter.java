package com.tfm.secureappspring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    ReCaptchaValidator reCaptchaValidator;
    private static final String SPRING_SECURITY_FORM_CAPTCHA_KEY = "g-recaptcha-response";

    private static final Log LOG = LogFactory.getLog(CustomAuthenticationFilter.class);

    private static final String ERROR_MESSAGE = "Something went wrong while parsing /login request body";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        CustomAuthenticationToken authRequest = getAuthRequest(request);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);

        /*String captcha = request.getParameter("g-recaptcha-response");
        if (!reCaptchaValidator.isValidCaptcha(captcha)) {
            throw new AuthenticationServiceException("Please validate captcha.");
        }*/
    }

    private CustomAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String username = request.getParameter("mail");
        String password = obtainPassword(request);
        String captcha = obtainCaptcha(request);

        if (username == null) {
            username = "";
        }
        if (password == null) {
            password = "";
        }
        if (captcha == null) {
            captcha = "";
        }

        return new CustomAuthenticationToken(username, password, captcha);
    }

    private String obtainCaptcha(HttpServletRequest request) {
        return request.getParameter(SPRING_SECURITY_FORM_CAPTCHA_KEY);
    }
}
