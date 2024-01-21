package test.Services;

import framework.annotations.Autowired;
import framework.annotations.Component;
import framework.annotations.Qualifier;

@Component
@Qualifier("ComponentC")
public class ComponentC implements ComponentInter {

    @Autowired(verbose = true)
    @Qualifier("ServiceC")
    private ServiceInter serviceC;

    public ComponentC() {
    }
}
