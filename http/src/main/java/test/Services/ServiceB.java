package test.Services;


import framework.annotations.Autowired;
import framework.annotations.Qualifier;
import framework.annotations.Service;

@Service
@Qualifier("ServiceB")
public class ServiceB implements ServiceInter {

    @Autowired(verbose = true)
    private ComponentB componentB;

    public String letComponentBDoSomething(){
        return "DONE";
    }
    public ServiceB() {
    }

}
