package com.delllogistics.controller;

import com.delllogistics.dto.Greeting;
import com.delllogistics.dto.HelloMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 * Created by IntelliJ IDEA.<br/>
 * User: jiajie<br/>
 * Date: 12/02/2018<br/>
 * Time: 4:48 PM<br/>
 */
@Controller
public class GreetingController {

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }

}
