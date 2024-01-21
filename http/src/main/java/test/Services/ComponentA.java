package test.Services;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean(scope = "prototype")
@Qualifier("ComponentA")
public class ComponentA implements ComponentInter {

    @Autowired(verbose = true)
    private ComponentC componentC;

    public ComponentA() {
    }
}
