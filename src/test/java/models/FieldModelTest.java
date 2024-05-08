package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldNode;
import domain.model.FieldModel;

public class FieldModelTest {

@Test
void testFieldModel() {
    // Create a FieldNode for testing
    FieldNode fieldNodeMock = Mockito.mock(FieldNode.class);
    fieldNodeMock.name = "TestFieldName";
    fieldNodeMock.access = Opcodes.ACC_PUBLIC;
    fieldNodeMock.desc = "I";
    fieldNodeMock.signature = "null";

    // Create a MethodModel instance
    FieldModel fieldModel = new FieldModel(fieldNodeMock);

    // Test getters
    assertEquals("TestFieldName", fieldModel.getName());
    assertEquals("I", fieldModel.getDesc());
    assertEquals("I", fieldModel.getType());
    assertEquals("null", fieldModel.getSignature());

    // Test Privitize and Publicize
    assertTrue(fieldModel.isPublic());
    fieldModel.privatize();
    assertTrue(fieldModel.isPrivate());
    fieldModel.publicize();
    assertFalse(fieldModel.isPrivate());

    // Test access
    assertFalse(fieldModel.isProtected());
    assertFalse(fieldModel.isStatic());
    assertFalse(fieldModel.isFinal());

}

}
