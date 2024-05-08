package models;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

import domain.model.ClassModel;

import java.util.ArrayList;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class ClassModelTest {

    @Test
    void testClassModel() {
        // Create a ClassNode for testing
        ClassNode classNodeMock = Mockito.mock(ClassNode.class);
        classNodeMock.name = "TestClassName";
        classNodeMock.superName = "SuperClassName";
        classNodeMock.interfaces = new ArrayList<String>();
        classNodeMock.access = Opcodes.ACC_PUBLIC;

        // Create a ClassModel instance
        ClassModel classModel = new ClassModel(classNodeMock);

        // Test getters
        assertEquals("TestClassName", classModel.getName());
        assertEquals("SuperClassName", classModel.getSuperName());
        assertTrue(classModel.getInterfaces().isEmpty());
        assertTrue(classModel.getMethods().isEmpty());
        assertTrue(classModel.getFields().isEmpty());
        assertTrue(classModel.getAbstractMethods().isEmpty());

        // Test access
        assertTrue(classModel.isPublic());
        assertFalse(classModel.isPrivate());
        assertFalse(classModel.isProtected());
        assertFalse(classModel.isStatic());
        assertFalse(classModel.isFinal());
        assertFalse(classModel.isAbstract());
        assertFalse(classModel.isInterface());

        // Test saving to file (This method will save a file with ".class" extension to the path "files/ClassFiles/")
        assertDoesNotThrow(classModel::saveModelToFile);
    }
}

