package com.github.johanfredin.bindygenerator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.FileNotFoundException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

@RunWith(JUnit4::class)
class BindyGeneratorTest {

    private val source = Paths.get("src/test/resources/person.csv")
    private val expectedWithColumns = Paths.get("src/test/resources/ExpectedResult_WithColumnNames.java")
    private val expectedWithoutColumns = Paths.get("src/test/resources/ExpectedResult_WithoutColumnNames.java")
    private val javaSourceFilePath = Paths.get("src/test/resources/BindyPerson.java")
    private val javaSourceFilePathNoColumns = Paths.get("src/test/resources/BindyPerson_NoColumns.java")

    @Test
    @Throws(FileNotFoundException::class)
    fun testGenerate() {
        getGenerator().generate()
        verifyResultFileLooksTheSameAsExpected(javaSourceFilePath, expectedWithColumns)
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun testGenerate_NoColumnNames() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                includeColumnName = false,
                isHeader = true,
                integerType = IntegerType.INTEGER,
                decimalType = DecimalType.FLOAT,
                stringType = `StringType.java`.STRING)

        val generator = getGenerator(config, source, javaSourceFilePathNoColumns)
        generator.generate()

        verifyResultFileLooksTheSameAsExpected(javaSourceFilePathNoColumns, expectedWithoutColumns)
    }

    private fun verifyResultFileLooksTheSameAsExpected(javaSourceFilePath: Path, expectedWithColumns: Path) {

        var resultContent = ""
        Files.lines(javaSourceFilePath)
                .forEach{line -> resultContent += line}
        var expectedFileContent = ""
        Files.lines(expectedWithColumns)
                .forEach { line -> expectedFileContent += line }
        assertEquals("File content is the same", resultContent, expectedFileContent)
    }

    @Test
    fun getFieldMapFromFile_hasHeader_useNumericFieldValues_ObjectTypes() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                includeColumnName = true,
                isHeader = true,
                integerType = IntegerType.INTEGER,
                decimalType = DecimalType.FLOAT,
                stringType = `StringType.java`.STRING)

        val generator = getGenerator(config)

        val fieldMapFromFile = generator.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=Integer", IntegerType.INTEGER.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=Float when mixed int and decimal types",
                DecimalType.FLOAT.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                `StringType.java`.STRING.label, fieldMapFromFile[3]?.type)

        assertEquals(0, fieldMapFromFile[0]?.pos)
        assertEquals(1, fieldMapFromFile[1]?.pos)
        assertEquals(2, fieldMapFromFile[2]?.pos)
        assertEquals(3, fieldMapFromFile[3]?.pos)
    }

    @Test
    fun getFieldMapFromFile_hasHeader_useNumericFieldValues_PrimitiveTypes() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                includeColumnName = true,
                isHeader = true,
                integerType = IntegerType.INTEGER_PRIMITIVE,
                decimalType = DecimalType.FLOAT_PRIMITIVE,
                stringType = `StringType.java`.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=int", IntegerType.INTEGER_PRIMITIVE.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=float when mixed int and decimal types",
                DecimalType.FLOAT_PRIMITIVE.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                `StringType.java`.STRING.label, fieldMapFromFile[3]?.type)

        assertEquals(0, fieldMapFromFile[0]?.pos)
        assertEquals(1, fieldMapFromFile[1]?.pos)
        assertEquals(2, fieldMapFromFile[2]?.pos)
        assertEquals(3, fieldMapFromFile[3]?.pos)
    }

    @Test
    fun testFieldMapFromFile_noHeader_DoNotUseNumericFieldValues() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.AS_IS,
                includeColumnName = true,
                isHeader = false,
                integerType = IntegerType.DEFAULT,
                decimalType = DecimalType.DEFAULT,
                stringType = `StringType.java`.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                String::class.java.simpleName, fieldMapFromFile[3]?.type)

        assertEquals(0, fieldMapFromFile[0]?.pos)
        assertEquals(1, fieldMapFromFile[1]?.pos)
        assertEquals(2, fieldMapFromFile[2]?.pos)
        assertEquals(3, fieldMapFromFile[3]?.pos)
    }

    @Test
    fun testFieldMapFromFile_noHeader_UseNumericFieldValues_PrimitiveTypes() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.AS_IS,
                includeColumnName = true,
                isHeader = false,
                integerType = IntegerType.INTEGER_PRIMITIVE,
                decimalType = DecimalType.FLOAT_PRIMITIVE,
                stringType = `StringType.java`.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", `StringType.java`.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=int", IntegerType.INTEGER_PRIMITIVE.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=float when mixed int and decimal types",
                DecimalType.FLOAT_PRIMITIVE.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                `StringType.java`.STRING.label, fieldMapFromFile[3]?.type)

        assertEquals(0, fieldMapFromFile[0]?.pos)
        assertEquals(1, fieldMapFromFile[1]?.pos)
        assertEquals(2, fieldMapFromFile[2]?.pos)
        assertEquals(3, fieldMapFromFile[3]?.pos)
    }

    @Test
    fun testGetHeaderField_LowerCamelCase() {
        val headerField = getGenerator().getHeaderField("APEX_TWIN", FieldMapping.LOWER_CAMEL_CASE)
        assertEquals("should be APEX_TWIN", "APEX_TWIN", headerField.dataSourceName)
        assertEquals("Should now be apexTwin", "apexTwin", headerField.javaFieldName)
    }

    @Test
    fun testGetHeaderField_AsIs_QuoteCharsRemoved() {
        val headerField = getGenerator().getHeaderField("'\"MY_MONKEY\"'", FieldMapping.AS_IS)
        assertEquals("Should be MY_MONKEY", "MY_MONKEY", headerField.dataSourceName)
        assertEquals("Should now be MY_MONKEY", "MY_MONKEY", headerField.javaFieldName)
    }

    @Test
    fun testGetHeaderField_LowerCamelCase_IllegalDelimitersRemoved() {
        val headerField = getGenerator().getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.LOWER_CAMEL_CASE)
        assertEquals("Should be MY-LITTLE MONKEY", "MY-LITTLE MONKEY", headerField.dataSourceName)
        assertEquals("Should now be myLittleMonkey", "myLittleMonkey", headerField.javaFieldName)
    }

    @Test(expected = RuntimeException::class)
    fun testGetHeaderField_AsIs_IllegalDelimitersFound() {
        val headerField = getGenerator().getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.AS_IS)
        assertNull("Header field could not be transformed", headerField.javaFieldName)
    }

    @Test
    fun testGetFieldType_PrimitiveTypes() {
        val generator = getGenerator()
        assertEquals(FieldType.STRING, generator.getType("Hello"))
        assertEquals(FieldType.INTEGER, generator.getType("25"))
        assertEquals(FieldType.DECIMAL, generator.getType("24.5"))
    }

    @Test
    fun testGetFieldType_ObjectTypes() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.AS_IS,
                includeColumnName = true,
                isHeader = true,
                integerType = IntegerType.INTEGER,
                decimalType = DecimalType.FLOAT,
                stringType = `StringType.java`.STRING)
        val generator = getGenerator(config)
        assertEquals(FieldType.STRING, generator.getType("Hello"))
        assertEquals(FieldType.INTEGER, generator.getType("25"))
        assertEquals(FieldType.DECIMAL, generator.getType("24.5"))
    }

    @Test
    fun testMappingToEnumCorrect() {
        val config = getConfig(
                integerType = IntegerType.INTEGER,
                decimalType = DecimalType.FLOAT,
                stringType = `StringType.java`.STRING)
        val generator = getGenerator(config)
        assertEquals(`StringType.java`.STRING.label, generator.mapToCorrespondingFieldType(generator.getType("Hello")))
        assertEquals(IntegerType.INTEGER.label, generator.mapToCorrespondingFieldType(generator.getType("25")))
        assertEquals(DecimalType.FLOAT.label, generator.mapToCorrespondingFieldType(generator.getType("24.5")))
    }

    @Test
    fun testMappingToEnumCorrect_primitiveTypes() {
        val config = getConfig(
                integerType = IntegerType.SHORT_PRIMITIVE,
                decimalType = DecimalType.DOUBLE_PRIMITIVE,
                stringType = `StringType.java`.CHAR_SEQUENCE)
        val generator = getGenerator(config)
        assertEquals(`StringType.java`.CHAR_SEQUENCE.label, generator.mapToCorrespondingFieldType(generator.getType("Hello")))
        assertEquals(IntegerType.SHORT_PRIMITIVE.label, generator.mapToCorrespondingFieldType(generator.getType("25")))
        assertEquals(DecimalType.DOUBLE_PRIMITIVE.label, generator.mapToCorrespondingFieldType(generator.getType("24.5")))
    }

    private fun getConfig(delimiter: String = ";",
                          javaClassName: String = "BindyPerson",
                          fieldMapping: FieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                          isHeader: Boolean = true,
                          includeColumnName: Boolean = true,
                          decimalType: DecimalType = DecimalType.FLOAT_PRIMITIVE,
                          integerType: IntegerType = IntegerType.INTEGER_PRIMITIVE,
                          stringType: `StringType.java` = `StringType.java`.STRING): GeneratorConfig {
        return GeneratorConfig(delimiter, javaClassName, fieldMapping, isHeader, includeColumnName, integerType, decimalType, stringType)
    }

    private fun getConfig(
            decimalType: DecimalType,
            integerType: IntegerType,
            stringType: `StringType.java`): GeneratorConfig {
        return GeneratorConfig(delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.AS_IS,
                isIncludeColumnName = true,
                isHeader = true,
                decimalType = decimalType,
                integerType = integerType,
                stringType = stringType)
    }

    private fun getGenerator(generatorConfig: GeneratorConfig = getConfig(),
                             source: Path = this.source,
                             javaFilePath: Path = this.javaSourceFilePath): BindyGenerator {
        return BindyGenerator(generatorConfig, source, javaFilePath)
    }
}
