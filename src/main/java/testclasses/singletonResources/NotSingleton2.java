package testclasses.singletonResources;

public class NotSingleton2 {
    private String var;
    private NotSingleton2(){}
    public String getInst(){
        if(var == null){
            var = "foo";
        }
        return var;
    }
}
