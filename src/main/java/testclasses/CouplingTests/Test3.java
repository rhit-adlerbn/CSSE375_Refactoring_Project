package testclasses.CouplingTests;

import testclasses.*;
import testclasses.Other.CouplingTestHelper1;
import testclasses.Other.CouplingTestHelper2;
import testclasses.Other.CouplingTestHelper3;
import testclasses.observer.Observer;
import testclasses.observer.Subject;

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
