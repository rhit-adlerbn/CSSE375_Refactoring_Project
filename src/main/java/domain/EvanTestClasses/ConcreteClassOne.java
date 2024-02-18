package domain.EvanTestClasses;

import domain.EvanTestClasses.Abstraction;

public class ConcreteClassOne extends Abstraction {
    public void stepIfImplDifferBySubclassM1(){
        System.out.println("HI");
    }
    public void stepIfImplDifferBySubclassM2(){
        System.out.println("Woof");
    }
}
