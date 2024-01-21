package test.Controllers;

import framework.annotations.*;
import test.Services.ServiceA;
import test.Services.ServiceB;
import test.Services.ServiceInter;

@Controller
public class B {

    @Autowired(verbose = true)
    private ServiceA serviceA;

    @Autowired(verbose = true)
    private ServiceB serviceB;

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private ServiceInter serviceC;


    @Post
    @Path(path = "/method33")
    public void method2(String testParam){
        serviceA.methodA1(testParam);
    }
}
