package com.boyceliu.api;

import com.boyceliu.autoclick.service.AutoClickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/1.0/autoclick")
public class AutoClickController {
    @Autowired
    private AutoClickService autoClickService;

    @RequestMapping(method = RequestMethod.GET, path = "/run")
    public ResponseEntity autoClick() {
        autoClickService.autoClick();
        return new ResponseEntity(HttpStatus.OK);
    }
}
