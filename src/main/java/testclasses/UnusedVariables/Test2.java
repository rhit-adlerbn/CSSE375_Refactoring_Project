package testclasses.UnusedVariables;

public class Test2 {

    private int happy;
    public Test2(int yes){
        happy = yes;

    }

    public void doStuff(){
        int notUnused = 10;
        int helpful = 20;

        helpful += notUnused;

        System.out.println(helpful);

    }
}
