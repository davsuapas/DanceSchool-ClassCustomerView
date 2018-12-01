package com.elipcero.classcustomerviewschool.controller.test;

import com.elipcero.classcustomerviewschool.repository.CustomerClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("smoke")
@RestController
@RequestMapping("/smokeverifier")
public class SmokeVerifier {

    @Autowired private MongoOperations mongoOperations;
    @Autowired private CustomerClassRepository customerClassRepository;

    @GetMapping
    public int jobDone() {
        return customerClassRepository
                .findById(1)
                .map(c -> c.getClasses().size())
                .orElse(0);
    }

    @DeleteMapping
    public void Clean() {
        mongoOperations.dropCollection("CustomerClass");
        mongoOperations.dropCollection("ClassCustomerDayTotal");
    }
}
