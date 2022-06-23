package com.tfm.secureappspring.controllers;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Controller
public class MyErrorController implements ErrorController {

    @GetMapping(value = "/error")
    public String handleError(HttpServletRequest request) {

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        Integer statusCode = null;
        Integer httpStatusCode = null;

        if (status != null) {
            statusCode = getStatusCode(status);
        }
        if (status == null && request.getQueryString() != null) {
            String[] httpStatusRedirect = request.getQueryString().split("=");
            if (httpStatusRedirect[1] != null) {
                httpStatusCode = getHttpStatusCode(httpStatusRedirect[1]);
            }
        }

            if(Objects.equals(statusCode, HttpStatus.NOT_FOUND.value()) || Objects.equals(httpStatusCode, 404)) {
                return "error-404";
            }
            else if(Objects.equals(statusCode, HttpStatus.INTERNAL_SERVER_ERROR.value()) ||
                    Objects.equals(httpStatusCode, 500)) {
                return "error-500";
            } else if (Objects.equals(statusCode, HttpStatus.BAD_REQUEST.value()) ||
                    Objects.equals(httpStatusCode, 400)) {
                return "error-400";
            }
        return "error";
    }

    private Integer getStatusCode(Object status) {
        return Integer.valueOf(status.toString());
    }

    private Integer getHttpStatusCode(String httpStatusRedirect) {
        Integer httpStatusCode;
        try {
            httpStatusCode = Integer.parseInt(httpStatusRedirect);
            return httpStatusCode;
        } catch (NumberFormatException e) {
            return 400;
        }
    }
}