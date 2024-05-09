package models;

import org.junit.jupiter.api.Test;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;

import domain.model.MethodModel;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class MethodModelTest {

    @Test
    void testMethodModel() {
        // Create a ModelNode for testing
        MethodNode methodNodeMock = Mockito.mock(MethodNode.class);
        methodNodeMock.name = "TestMethodName";
        methodNodeMock.access = Opcodes.ACC_PUBLIC;
        methodNodeMock.desc = "(Ltestclasses/AccessModClasses/AccessModClassA;)V";

        // Create a MethodModel instance
        MethodModel methodModel = new MethodModel(methodNodeMock);

        // Test getters
        assertEquals("TestMethodName", methodModel.getName());
        assertEquals("(Ltestclasses/AccessModClasses/AccessModClassA;)V", methodModel.getDesc());
        assertEquals(1, methodModel.getParams().size());
        assertEquals(0, methodModel.getLocalVars().size());
        assertEquals("void", methodModel.getReturnType());

        // Test Privitize and Publicize
        assertTrue(methodModel.isPublic());
        methodModel.privatize();
        assertTrue(methodModel.isPrivate());
        methodModel.publicize();
        assertFalse(methodModel.isPrivate());

        // Test access
        assertFalse(methodModel.isProtected());
        assertFalse(methodModel.isStatic());
        assertFalse(methodModel.isFinal());

    }
}

