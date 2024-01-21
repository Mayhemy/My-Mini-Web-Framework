package test.Services;

import framework.annotations.Autowired;
import framework.annotations.Component;
import framework.annotations.Qualifier;

@Component
@Qualifier("ComponentB")
public class ComponentB implements ComponentInter {

    @Autowired(verbose = true)
    @Qualifier("ComponentC")
    private ComponentInter componentC;

    public ComponentB() {
    }
}
