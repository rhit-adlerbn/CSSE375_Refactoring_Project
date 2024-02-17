package TestSource.TestRun;
import datasource.ASMAdapter;
import domain.ClassModel;
import domain.GrammarCheck;
import domain.PatternCheck;
import org.junit.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.*;
public class ObserverTests {



    @Test
    public void testSubject() throws IOException {
        ArrayList<ClassModel> test = ASMAdapter.parseASM("C:\\Users\\taskand\\IdeaProjects\\project-202420-s3-team15-202420\\src\\main\\java\\SubjectTest");
        for(ClassModel clas: test){

            Assert.assertEquals("Subject", PatternCheck.checkObserverPattern(clas));
        }


    }
    public void testObserver() {



    }
    public void testNonObserver() {



    }
}
