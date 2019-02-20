package com.github.johanfredin.bindygenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class BindyGeneratorTest {

    private BindyGenerator generator;
    private Path expectedWithColumns;
    private Path expectedWitoutColumns;
    private Path javaSourceFilePath;
    private Path javaSourceFilePath_NoColumns;

    @Before
    public void init() {
        GeneratorConfig config = getConfig();
        File soure = Paths.get("src/test/resources/person.csv").toFile();
        this.expectedWithColumns = Paths.get("src/test/resources/ExpectedResult_WithColumnNames.java");
        this.expectedWitoutColumns = Paths.get("src/test/resources/ExpectedResult_WithoutColumnNames.java");
        this.javaSourceFilePath = Paths.get("src/test/resources/BindyPerson.java");
        this.javaSourceFilePath_NoColumns = Paths.get("src/test/resources/BindyPerson_NoColumns.java");
        this.generator = new BindyGenerator(config, soure, javaSourceFilePath);
    }

    @Test
    public void testGenerate() throws FileNotFoundException {
        this.generator.generate();
        File result = javaSourceFilePath.toFile();
        File expectedFile = expectedWithColumns.toFile();

        String resultContent = "";
        Scanner sc = new Scanner(result);
        while(sc.hasNextLine()) {
            resultContent += sc.nextLine();
        }
        sc.close();

        String expectedFileContent = "";
        sc = new Scanner(expectedFile);
        while(sc.hasNextLine()) {
            expectedFileContent += sc.nextLine();
        }
        sc.close();

        assertEquals("File content is the same", resultContent, expectedFileContent);
    }

    @Test
    public void testGenerate_NoColumnNames() throws FileNotFoundException {
        this.generator.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, false, false));

        this.generator.setJavaSourceFilePath(javaSourceFilePath_NoColumns);
        this.generator.generate();
        File result = javaSourceFilePath_NoColumns.toFile();
        File expectedFile = expectedWitoutColumns.toFile();

        String resultContent = "";
        Scanner sc = new Scanner(result);
        while(sc.hasNextLine()) {
            resultContent += sc.nextLine();
        }
        sc.close();

        String expectedFileContent = "";
        sc = new Scanner(expectedFile);
        while(sc.hasNextLine()) {
            expectedFileContent += sc.nextLine();
        }
        sc.close();

        assertEquals("File content is the same", resultContent, expectedFileContent);
    }

    @Test
    public void getFieldMapFromFile_hasHeader_useNumericFieldValues_ObjectTypes() {
        this.generator.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, false, true));

        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=name","name", fieldMapFromFile.get(0).getJavaFieldName());
        assertEquals("Field name=age","age", fieldMapFromFile.get(1).getJavaFieldName());
        assertEquals("Field name=tax","tax", fieldMapFromFile.get(2).getJavaFieldName());
        assertEquals("Field name=mixedBag","mixedBag", fieldMapFromFile.get(3).getJavaFieldName());

        assertEquals("Field type=String",String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field type=Integer",Integer.class.getSimpleName(), fieldMapFromFile.get(1).getType());
        assertEquals("Field type=Float when mixed int and decimal types",
                Float.class.getSimpleName(), fieldMapFromFile.get(2).getType());
        assertEquals("Field type=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());

        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    @Test
    public void getFieldMapFromFile_hasHeader_useNumericFieldValues_PrimitiveTypes() {
        this.generator.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, true, true));

        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=name","name", fieldMapFromFile.get(0).getJavaFieldName());
        assertEquals("Field name=age","age", fieldMapFromFile.get(1).getJavaFieldName());
        assertEquals("Field name=tax","tax", fieldMapFromFile.get(2).getJavaFieldName());
        assertEquals("Field name=mixedBag","mixedBag", fieldMapFromFile.get(3).getJavaFieldName());
        assertEquals("Field type=String",String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field type=int","int", fieldMapFromFile.get(1).getType());
        assertEquals("Field type=float when mixed int and decimal types",
                "float", fieldMapFromFile.get(2).getType());
        assertEquals("Field type=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());
        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    @Test
    public void testFieldMapFromFile_noHeader_DoNotUseNumericFieldValues() {
        this.generator.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.AS_IS,
                        false, false, true, true));

        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=COLUMN_0","COLUMN_0", fieldMapFromFile.get(0).getJavaFieldName());
        assertEquals("Field name=COLUMN_1","COLUMN_1", fieldMapFromFile.get(1).getJavaFieldName());
        assertEquals("Field name=COLUMN_2","COLUMN_2", fieldMapFromFile.get(2).getJavaFieldName());
        assertEquals("Field name=COLUMN_3","COLUMN_3", fieldMapFromFile.get(3).getJavaFieldName());
        assertEquals("Field type=String", String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field type=String", String.class.getSimpleName(), fieldMapFromFile.get(1).getType());
        assertEquals("Field type=String", String.class.getSimpleName(), fieldMapFromFile.get(2).getType());
        assertEquals("Field type=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());
        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    @Test
    public void testFieldMapFromFile_noHeader_UseNumericFieldValues_PrimitiveTypes() {
        this.generator.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.AS_IS,
                        false, true, true, true));

        Map<Integer, BindyField> fieldMapFromFile = this.generator.getFieldMapFromFile();
        assertEquals("Size=4", 4, fieldMapFromFile.size());
        assertEquals("Field name=COLUMN_0","COLUMN_0", fieldMapFromFile.get(0).getJavaFieldName());
        assertEquals("Field name=COLUMN_1","COLUMN_1", fieldMapFromFile.get(1).getJavaFieldName());
        assertEquals("Field name=COLUMN_2","COLUMN_2", fieldMapFromFile.get(2).getJavaFieldName());
        assertEquals("Field name=COLUMN_3","COLUMN_3", fieldMapFromFile.get(3).getJavaFieldName());

        assertEquals("Field type=String",String.class.getSimpleName(), fieldMapFromFile.get(0).getType());
        assertEquals("Field type=int","int", fieldMapFromFile.get(1).getType());
        assertEquals("Field type=float when mixed int and decimal types",
                "float", fieldMapFromFile.get(2).getType());
        assertEquals("Field type=String when at least one field is a String",
                String.class.getSimpleName(), fieldMapFromFile.get(3).getType());

        assertEquals(0, fieldMapFromFile.get(0).getPos());
        assertEquals(1, fieldMapFromFile.get(1).getPos());
        assertEquals(2, fieldMapFromFile.get(2).getPos());
        assertEquals(3, fieldMapFromFile.get(3).getPos());
    }

    @Test
    public void testGetHeaderField_LowerCamelCase() {
        DatasetHeader headerField = this.generator.getHeaderField("APEX_TWIN", FieldMapping.LOWER_CAMEL_CASE);
        assertEquals("should be APEX_TWIN", "APEX_TWIN", headerField.getDataSourceName());
        assertEquals("Should now be apexTwin", "apexTwin", headerField.getJavaFieldName());
    }

    @Test
    public void testGetHeaderField_AsIs_QuoteCharsRemoved() {
        DatasetHeader headerField = this.generator.getHeaderField("'\"MY_MONKEY\"'", FieldMapping.AS_IS);
        assertEquals("Should be MY_MONKEY", "MY_MONKEY", headerField.getDataSourceName());
        assertEquals("Should now be MY_MONKEY", "MY_MONKEY", headerField.getJavaFieldName());
    }

    @Test
    public void testGetHeaderField_LowerCamelCase_IllegalDelimitersRemoved() {
        DatasetHeader headerField = this.generator.getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.LOWER_CAMEL_CASE);
        assertEquals("Should be MY-LITTLE MONKEY", "MY-LITTLE MONKEY", headerField.getDataSourceName());
        assertEquals("Should now be myLittleMonkey", "myLittleMonkey", headerField.getJavaFieldName());
    }

    @Test(expected = RuntimeException.class)
    public void testGetHeaderField_AsIs_IllegalDelimitersFound() {
        DatasetHeader headerField = this.generator.getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.AS_IS);
        assertNull("Header field could not be transformed", headerField.getJavaFieldName());
    }

    private GeneratorConfig getConfig() {
        return getConfig(";",
                "BindyPerson",
                FieldMapping.LOWER_CAMEL_CASE,
                true,
                true,
                true,
                true);
    }

    private GeneratorConfig getConfig(String delimiter, String javaClassName, FieldMapping fieldMapping,
                                      boolean isHeader, boolean useNumericFieldTypes,
                                      boolean usePrimitiveTypesWherePossible,
                                      boolean includeColumnName) {
        return new GeneratorConfig(delimiter, javaClassName, fieldMapping, isHeader, useNumericFieldTypes, usePrimitiveTypesWherePossible, includeColumnName);
    }
}