package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zjp.translateit.domain.Donation;
import zjp.translateit.service.ManageService;
import zjp.translateit.web.response.Response;

@Controller
@RequestMapping("/manage")
public class ManageController {

    private final ManageService service;

    @Autowired
    public ManageController(ManageService service) {
        this.service = service;
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

}
