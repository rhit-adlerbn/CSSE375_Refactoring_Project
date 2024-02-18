package testclasses.singletonResources;

public class NotSingleton3 {
    private static NotSingleton3 inst;
    private NotSingleton3(){}
    public static NotSingleton3 get(){
        if(inst == null){
            inst = new NotSingleton3();
        }
        return inst;
    }
}
