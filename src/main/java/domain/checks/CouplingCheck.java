package domain.checks;

import domain.model.ClassModel;
import domain.model.FieldModel;
import domain.model.MethodModel;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CouplingCheck implements LintCheck{

    public List<String> runLintCheck(List<ClassModel> classes){
        ArrayList<String> msgs = new ArrayList<String>();
        for(ClassModel clas: classes){
            msgs.add(checkCoupling(clas));
        }
        return msgs;
    }

    public String checkCoupling(ClassModel classNode){
        double couplingScore = 0;
        List<FieldModel> fields =  classNode.getFields();
        HashMap<String, Integer> Primary =  new HashMap<String, Integer>();
        Primary.put("String", 1);
        Primary.put("int", 1);
        Primary.put("char", 1);
        Primary.put("long", 1);
        Primary.put("double", 1);
        for (FieldModel field : fields) {
            Type fieldType;
            if(field.getSignature() == null) {
                fieldType = Type.getType(field.getDesc());
            } else {
                fieldType = Type.getType(field.getSignature());
            }
            String name = fieldType.getClassName();
            if(!Primary.containsKey(name)){
                couplingScore*=1.4;
                couplingScore++;

            }
        }
        return ""+couplingScore;
    }
}
