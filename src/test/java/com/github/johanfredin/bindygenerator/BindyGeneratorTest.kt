package com.github.johanfredin.bindygenerator

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

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
        val result = javaSourceFilePath.toFile()
        val expectedFile = expectedWithColumns.toFile()

        var resultContent = ""
        var sc = Scanner(result)
        while (sc.hasNextLine()) {
            resultContent += sc.nextLine()
        }
        sc.close()

        var expectedFileContent = ""
        sc = Scanner(expectedFile)
        while (sc.hasNextLine()) {
            expectedFileContent += sc.nextLine()
        }
        sc.close()

        assertEquals("File content is the same", resultContent, expectedFileContent)
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun testGenerate_NoColumnNames() {
        val config = getConfig(
                delimiter = ";",
                javaClassName = "Jonsson",
                fieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                includeColumnName = true,
                isHeader = false,
                integerType = IntegerType.INTEGER,
                decimalType = DecimalType.FLOAT,
                stringType = StringType.STRING)

        val generator = getGenerator(config, source, javaSourceFilePathNoColumns)
        generator.generate()
        val result = javaSourceFilePathNoColumns.toFile()
        val expectedFile = expectedWithoutColumns.toFile()

        var resultContent = ""
        var sc = Scanner(result)
        while (sc.hasNextLine()) {
            resultContent += sc.nextLine()
        }
        sc.close()

        var expectedFileContent = ""
        sc = Scanner(expectedFile)
        while (sc.hasNextLine()) {
            expectedFileContent += sc.nextLine()
        }
        sc.close()

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
                stringType = StringType.STRING)

        val generator = getGenerator(config)

        val fieldMapFromFile = generator.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", StringType.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=Integer", IntegerType.INTEGER.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=Float when mixed int and decimal types",
                DecimalType.FLOAT.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                StringType.STRING.label, fieldMapFromFile[3]?.type)

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
                stringType = StringType.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", FieldType.STRING, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=int", FieldType.INTEGER, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=float when mixed int and decimal types",
                FieldType.DECIMAL, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                FieldType.STRING, fieldMapFromFile[3]?.type)

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
                stringType = StringType.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", StringType.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=String", StringType.STRING.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=String", StringType.STRING.label, fieldMapFromFile[2]?.type)
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
                stringType = StringType.STRING)

        val fieldMapFromFile = getGenerator(config).fieldMapFromFile

        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0]?.javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1]?.javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2]?.javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3]?.javaFieldName)

        assertEquals("Field objectTypeName=String", StringType.STRING.label, fieldMapFromFile[0]?.type)
        assertEquals("Field objectTypeName=int", DecimalType.FLOAT_PRIMITIVE.label, fieldMapFromFile[1]?.type)
        assertEquals("Field objectTypeName=float when mixed int and decimal types",
                DecimalType.FLOAT_PRIMITIVE.label, fieldMapFromFile[2]?.type)
        assertEquals("Field objectTypeName=String when at least one field is a String",
                StringType.STRING.label, fieldMapFromFile[3]?.type)

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
                stringType = StringType.STRING)
        val generator = getGenerator(config)
        assertEquals(FieldType.STRING, generator.getType("Hello"))
        assertEquals(FieldType.INTEGER, generator.getType("25"))
        assertEquals(FieldType.DECIMAL, generator.getType("24.5"))
    }

    private fun getConfig(delimiter: String = ";",
                          javaClassName: String = "BindyPerson",
                          fieldMapping: FieldMapping = FieldMapping.LOWER_CAMEL_CASE,
                          isHeader: Boolean = true,
                          includeColumnName: Boolean = true,
                          decimalType: DecimalType = DecimalType.FLOAT_PRIMITIVE,
                          integerType: IntegerType = IntegerType.INTEGER_PRIMITIVE,
                          stringType: StringType = StringType.STRING): GeneratorConfig {
        return GeneratorConfig(delimiter, javaClassName, fieldMapping, isHeader, includeColumnName, integerType, decimalType, stringType)
    }

    private fun getGenerator(generatorConfig: GeneratorConfig = getConfig(),
                             source: Path = this.source,
                             javaFilePath: Path = this.javaSourceFilePath): BindyGenerator {
        return BindyGenerator(generatorConfig, source, javaFilePath)
    }
}