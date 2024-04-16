package testclasses.AccessModClasses;

public class AccessModClassB {

    private void tryCallPrivateMethod(AccessModClassA obj) {
        obj.privateMethod();
    }

    private void tryCallPublicMethod(AccessModClassA obj) {
        obj.publicMethod();
    }

    private int getPrivateField(AccessModClassA obj) {
        return obj.privateField;
    }

    private int getPublicField(AccessModClassA obj) {
        return obj.publicField;
    }

}
