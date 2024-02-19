package testclasses.observer;

import testclasses.Observer;
import testclasses.Subject;

public class Test2 implements Observer {
    Subject h;

    public Test2(Subject k){
        this.h = k;
    }

    public static void update(){}
}
