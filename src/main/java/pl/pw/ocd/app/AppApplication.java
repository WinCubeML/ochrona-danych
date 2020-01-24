package pl.pw.ocd.app;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Log
@SpringBootApplication(scanBasePackages = {
        "pl.pw.ocd.app.configuration", "pl.pw.ocd.app.controller", "pl.pw.ocd.app.exceptions", "pl.pw.ocd.app.logic", "pl.pw.ocd.app.model", "pl.pw.ocd.app.repositories", "pl.pw.ocd.app.service"
})
public class AppApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
