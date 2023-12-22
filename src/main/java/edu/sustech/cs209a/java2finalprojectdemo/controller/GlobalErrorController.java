package edu.sustech.cs209a.java2finalprojectdemo.controller;
import edu.sustech.cs209a.java2finalprojectdemo.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.rmi.ServerException;


@RestController
public class GlobalErrorController implements ErrorController {

    private static final Logger logger = LoggerFactory.getLogger(GlobalErrorController.class);

    @RequestMapping("/error")
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleClientException(BadRequestException ex, Model model) {
        // 记录错误日志
        logger.error("BadRequestException occurred: {}", ex.getMessage());

        // 设置错误信息
        model.addAttribute("error", "BadRequestException occurred");

        // 返回自定义的错误页面
        return "errors";
    }

    @ExceptionHandler(ServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleOtherException(ServerException ex, Model model) {
        // 记录错误日志
        logger.error("OtherException occurred: {}", ex.getMessage());

        // 设置错误信息
        model.addAttribute("error", "Internal Server Error");

        // 返回自定义的错误页面
        return "errors";
    }

    // 可以添加其他异常处理方法

}
