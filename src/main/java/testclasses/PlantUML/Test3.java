package testclasses.PlantUML;

public class Test3 {
    private Test2 first;
    private Test1 second;

    public Test3(){
        this.first = new Test2();
        this.second = new Test1();
    }

    private void Method1(int arg1){

    }

    public String Method2(int arg1, int arg2){

        return "It WORKS!!";
    }


}
