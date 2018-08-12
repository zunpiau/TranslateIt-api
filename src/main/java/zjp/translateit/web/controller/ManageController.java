package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Donation;
import zjp.translateit.service.ManageService;
import zjp.translateit.web.response.Response;

import static zjp.translateit.Constant.CACHE_NAME_DONATION;

@Controller
@RequestMapping("/manage")
public class ManageController {

    private final ManageService service;
    private final CacheManager cacheManager;

    @Autowired
    public ManageController(ManageService service, CacheManager cacheManager) {
        this.service = service;
        this.cacheManager = cacheManager;
    }

    @ResponseBody
    @GetMapping(value = "/count")
    public Response getSystemCount() {
        return new Response<>(service.getSystemCounter());
    }

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity login(@RequestParam String name) {
        return ResponseEntity.ok(service.login(name));
    }

    @ResponseBody
    @PostMapping(value = "/donation",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response addDonation(@RequestBody Donation donation) {
        service.addDonation(donation);
        return new Response(Response.ResponseCode.OK);
    }

    @ResponseBody
    @DeleteMapping(value = "/donation/cache",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Response evictCache() {
        Cache cache = cacheManager.getCache(CACHE_NAME_DONATION);
        if (cache != null) {
            cache.clear();
        }
        return new Response(Response.ResponseCode.OK);
    }

}
