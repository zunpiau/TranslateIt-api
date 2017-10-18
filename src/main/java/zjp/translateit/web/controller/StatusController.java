package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    private final JdbcTemplate template;

    @Autowired
    public StatusController(JdbcTemplate template) {
        this.template = template;
    }

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity status() {
        return (template.queryForObject("SELECT 1", Integer.TYPE) == 1)
                ? new ResponseEntity(HttpStatus.OK)
                : new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}