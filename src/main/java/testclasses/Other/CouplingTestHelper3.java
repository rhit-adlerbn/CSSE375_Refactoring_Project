package testclasses.Other;

public class CouplingTestHelper3 {

    private CouplingTestHelper1 here;

    public CouplingTestHelper3(){

        this.here = new CouplingTestHelper1();
    }

    public double get(){
        return this.here.getHealth();
    }
}
