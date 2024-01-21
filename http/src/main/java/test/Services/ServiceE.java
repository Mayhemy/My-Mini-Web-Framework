package test.Services;

import framework.annotations.Bean;
import framework.annotations.Qualifier;

import java.io.File;
import java.io.IOException;

@Bean()
@Qualifier("ServiceE")
public class ServiceE implements ServiceInter {


    public ServiceE() {
    }
}
