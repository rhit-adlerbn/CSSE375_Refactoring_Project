package testclasses.Other;

public class CouplingTestHelper2 {

    private String name;

    public CouplingTestHelper2(){
        name = "James Smith";

    }


    public String getLastName(){

        return this.name.substring(5);
    }
}
