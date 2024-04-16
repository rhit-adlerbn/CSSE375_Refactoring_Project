package domain.model;



public abstract class Model{
    Object node;

    public abstract String getName();

    public abstract String getDesc();

    public abstract void privatize();

    public abstract boolean isPrivate();

    public abstract void publicize();
}