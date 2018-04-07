package zjp.translateit.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import zjp.translateit.data.DonationRepository;
import zjp.translateit.web.response.Response;

@RestController
public class MiscController {

    private final DonationRepository donationRepository;

    @Autowired
    public MiscController(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    @GetMapping("/donation")
    public Response getDonateList(@RequestParam int offset) {
        return new Response<>(donationRepository.listDonation(offset));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void handleBadParam() {
    }

}
