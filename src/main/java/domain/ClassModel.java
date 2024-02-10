package domain;

import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class ClassModel {
    private ClassNode node;

    public String getName() {
        return node.name;
    }
    public List<String> getInterfaces() {
        return node.interfaces;
    }
    public String getSuperName() {
        return node.superName;
    }


}
