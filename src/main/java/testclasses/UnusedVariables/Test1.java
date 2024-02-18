package testclasses.UnusedVariables;

public class Test1 {

    int hair;
    public Test1(){
        //this.hair = 10;

    }

    public void doFunny(int used, int unused){

        int notUsed = 0;
        notUsed = used;
        unused = 10;
        System.out.println(used);
    }
}
