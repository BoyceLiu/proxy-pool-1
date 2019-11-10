package com.boyceliu.api;

import com.boyceliu.proxypool.store.ObjectStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api")
public class ProxyApi {

    private static final String API_LIST =
            "get: get an usable proxy  "+
            "getAll: get proxy list  "+
            "delete?proxy=127.0.0.1:8080': delete an unable proxy " +
            "verify: verify the status of a proxy  ";

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public Response index(){
        return new Response(true, "", API_LIST);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/getAll")
    public Response getAll() {
        return new Response(true, "获取成功", ObjectStore.getInstance().getAll());
    }

    @GetMapping("/count")
    public Response getCount(){
        int count = ObjectStore.getInstance().size();
        return new Response(true, "获取成功", count);
    }
}
