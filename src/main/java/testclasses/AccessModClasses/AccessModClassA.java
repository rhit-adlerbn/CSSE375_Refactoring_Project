package testclasses.AccessModClasses;

public class AccessModClassA {
    private int privateField;
    public int publicField;
    public int unusedField;

    public int publicMethod() {
        return publicField;
    }

    private int privateMethod() {
        return privateField;
    }

    public void unusedMethod() {
        System.out.println("Unused method");
    }
}
