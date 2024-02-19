package testclasses.CouplingTests;

import testclasses.CouplingTestHelper1;
import testclasses.CouplingTestHelper2;

public class Test2 {
    private CouplingTestHelper2 littleCoupling;
    private CouplingTestHelper1 someCoupling;


    public Test2(){
        littleCoupling = new CouplingTestHelper2();
        someCoupling = new CouplingTestHelper1();
    }

}
