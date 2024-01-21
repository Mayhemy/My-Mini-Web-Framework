package test.Controllers;

import framework.annotations.*;
import test.Services.*;

import java.io.File;
import java.io.IOException;

@Controller
public class A {

    @Autowired(verbose = true)
    private ServiceA serviceA;

    @Autowired(verbose = true)
    private ServiceB serviceB;

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private ServiceInter serviceC;

    @Autowired(verbose = true)
    private ServiceE serviceE;

    public A() {
    }

    @Get
    @Path(path = "/method11")
    public void method1(){
        // Specify the file name
        serviceB.letComponentBDoSomething();
        ((ServiceC)serviceC).methodC1();
    }

    @Post
    @Path(path = "/method22")
    public void method2(String testParam){
        serviceA.methodA1(testParam);
    }
}
