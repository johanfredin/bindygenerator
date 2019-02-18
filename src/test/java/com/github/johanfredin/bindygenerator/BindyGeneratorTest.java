package com.github.johanfredin.bindygenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.nio.file.Paths;
import java.util.Map;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BindyGeneratorTest {

    private BindyGenerator generator;
    private File expected;

    @Before
    public void init() {
        GeneratorConfig config = getConfig();
        File soure = Paths.get("src/test/resources/person.csv").toFile();
        this.expected = Paths.get("src/test/resources/BindyPerson.java").toFile();
        this.generator = new BindyGenerator(config, soure);
    }

    @Test
    public void testGenerate() {
        File javaSourceFile = this.generator.generate();
        assertEquals("Generated java file same as expected java file", javaSourceFile, this.expected);
    }

    @Test
    public void getFieldMapFromFile_hasHeader_useNumericFieldValues() {
        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=name","name", fieldMapFromFile.get(0).getName());
        assertEquals("Field name=age","age", fieldMapFromFile.get(1).getName());
        assertEquals("Field name=tax","tax", fieldMapFromFile.get(2).getName());
        assertEquals("Field name=mixed-bag","mixed-bag", fieldMapFromFile.get(3).getName());
        assertEquals("Field type=String",String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field name=Integer",Integer.class.getSimpleName(), fieldMapFromFile.get(1).getType());
        assertEquals("Field name=Double when mixed int and double types",
                Double.class.getSimpleName(), fieldMapFromFile.get(2).getType());
        assertEquals("Field name=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());
        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    @Test
    public void testFieldMapFromFile_noHeader_DoNotUseNumericFieldValues() {
        this.generator.setGeneratorConfig(getConfig(";", false, false));
        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=COLUMN_0","COLUMN_0", fieldMapFromFile.get(0).getName());
        assertEquals("Field name=COLUMN_1","COLUMN_1", fieldMapFromFile.get(1).getName());
        assertEquals("Field name=COLUMN_2","COLUMN_2", fieldMapFromFile.get(2).getName());
        assertEquals("Field name=COLUMN_3","COLUMN_3", fieldMapFromFile.get(3).getName());
        assertEquals("Field type=String", String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field name=String", String.class.getSimpleName(), fieldMapFromFile.get(1).getType());
        assertEquals("Field name=String", String.class.getSimpleName(), fieldMapFromFile.get(2).getType());
        assertEquals("Field name=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());
        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    private GeneratorConfig getConfig() {
        return getConfig(";", true, true);
    }

    private GeneratorConfig getConfig(String delimiter, boolean isHeader, boolean useNumericFieldTypes) {
        return new GeneratorConfig(delimiter, isHeader, useNumericFieldTypes);
    }
}