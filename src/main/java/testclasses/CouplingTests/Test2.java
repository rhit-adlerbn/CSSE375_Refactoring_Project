package testclasses.CouplingTests;

import testclasses.Other.CouplingTestHelper1;
import testclasses.Other.CouplingTestHelper2;

public class Test2 {
    private CouplingTestHelper2 littleCoupling;
    private CouplingTestHelper1 someCoupling;


    public Test2(){
        littleCoupling = new CouplingTestHelper2();
        someCoupling = new CouplingTestHelper1();
    }

}
