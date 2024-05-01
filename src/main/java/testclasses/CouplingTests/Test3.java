package testclasses.CouplingTests;

import testclasses.Other.*;

public class Test3 {
    private Observer little;
    private Subject some;
    private CouplingTestHelper1 abithigh;
    private CouplingTestHelper2 veryhigh;
    private CouplingTestHelper3 extremhigh;

    public Test3(){
        abithigh = new CouplingTestHelper1();
        veryhigh = new CouplingTestHelper2();
        extremhigh = new CouplingTestHelper3();
    }

}
