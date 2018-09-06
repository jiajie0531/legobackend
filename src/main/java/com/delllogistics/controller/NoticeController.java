package com.delllogistics.controller;

import com.delllogistics.dto.PhoneValidateCode;
import com.delllogistics.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by jiajie on 10/06/2017.
 */
@RestController()
@RequestMapping("/notice")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @PostMapping(value = "/sendCode",produces = {"application/json;charset=utf-8"})
    public void sendCode(@RequestBody PhoneValidateCode phoneValidateCode) {
        noticeService.sendCode(phoneValidateCode);
    }
}

