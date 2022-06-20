package com.tfm.secureappspring;

import com.tfm.secureappspring.data.models.ReCaptchaKeys;
import com.tfm.secureappspring.data.models.ReCaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ReCaptchaValidator {

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ReCaptchaKeys reCaptchaKeys;

    public boolean isValidCaptcha(String captcha) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        String params = "?secret=" + reCaptchaKeys.getSecret() + "&response=" + captcha;
        String completeUrl = url + params;
        ReCaptchaResponse response = restTemplate.postForObject(completeUrl, null, ReCaptchaResponse.class);
        return response.isSuccess();
    }
}
