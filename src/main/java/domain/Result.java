package domain;

public class Result {
    private  String className;
    private  String testName;
    private  String testResult;

    public Result(String className, String test, String result){
        this.className = className;
        this.testName = test;
        this.testResult = result;
    }
    public String[] getResults(){
        return new String[]{className,testName,testResult};
    }
    public String toString(){
        return testResult;
    }
}
