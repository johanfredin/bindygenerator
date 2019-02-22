package com.github.johanfredin.bindygenerator

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

import java.io.File
import java.io.FileNotFoundException
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Scanner

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull

@RunWith(JUnit4::class)
class BindyGeneratorTest {

    private var generator: BindyGenerator? = null
    private var expectedWithColumns: Path? = null
    private var expectedWitoutColumns: Path? = null
    private var javaSourceFilePath: Path? = null
    private var javaSourceFilePath_NoColumns: Path? = null

    private val config: GeneratorConfig
        get() = getConfig(";",
                "BindyPerson",
                FieldMapping.LOWER_CAMEL_CASE,
                true,
                true,
                true,
                true)

    @Before
    fun init() {
        val config = config
        val soure = Paths.get("src/test/resources/person.csv")
        this.expectedWithColumns = Paths.get("src/test/resources/ExpectedResult_WithColumnNames.java")
        this.expectedWitoutColumns = Paths.get("src/test/resources/ExpectedResult_WithoutColumnNames.java")
        this.javaSourceFilePath = Paths.get("src/test/resources/BindyPerson.java")
        this.javaSourceFilePath_NoColumns = Paths.get("src/test/resources/BindyPerson_NoColumns.java")
        this.generator = BindyGenerator(config, soure, javaSourceFilePath)
    }

    @Test
    @Throws(FileNotFoundException::class)
    fun testGenerate() {
        this.generator!!.generate()
        val result = javaSourceFilePath!!.toFile()
        val expectedFile = expectedWithColumns!!.toFile()

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
        this.generator!!.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, false, false))

        this.generator!!.setJavaSourceFilePath(javaSourceFilePath_NoColumns)
        this.generator!!.generate()
        val result = javaSourceFilePath_NoColumns!!.toFile()
        val expectedFile = expectedWitoutColumns!!.toFile()

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
        this.generator!!.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, false, true))

        val fieldMapFromFile = this.generator!!.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0].javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1].javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2].javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3].javaFieldName)

        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[0].type)
        assertEquals("Field type=Integer", Int::class.java!!.getSimpleName(), fieldMapFromFile[1].type)
        assertEquals("Field type=Float when mixed int and decimal types",
                Float::class.java!!.getSimpleName(), fieldMapFromFile[2].type)
        assertEquals("Field type=String when at least one field is a String",
                String::class.java!!.getSimpleName(), fieldMapFromFile[3].type)

        assertEquals(0, fieldMapFromFile[0].pos.toLong())
        assertEquals(1, fieldMapFromFile[1].pos.toLong())
        assertEquals(2, fieldMapFromFile[2].pos.toLong())
        assertEquals(3, fieldMapFromFile[3].pos.toLong())
    }

    @Test
    fun getFieldMapFromFile_hasHeader_useNumericFieldValues_PrimitiveTypes() {
        this.generator!!.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.LOWER_CAMEL_CASE,
                        true, true, true, true))

        val fieldMapFromFile = this.generator!!.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=name", "name", fieldMapFromFile[0].javaFieldName)
        assertEquals("Field name=age", "age", fieldMapFromFile[1].javaFieldName)
        assertEquals("Field name=tax", "tax", fieldMapFromFile[2].javaFieldName)
        assertEquals("Field name=mixedBag", "mixedBag", fieldMapFromFile[3].javaFieldName)
        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[0].type)
        assertEquals("Field type=int", "int", fieldMapFromFile[1].type)
        assertEquals("Field type=float when mixed int and decimal types",
                "float", fieldMapFromFile[2].type)
        assertEquals("Field type=String when at least one field is a String",
                String::class.java!!.getSimpleName(), fieldMapFromFile[3].type)
        assertEquals(0, fieldMapFromFile[0].pos.toLong())
        assertEquals(1, fieldMapFromFile[1].pos.toLong())
        assertEquals(2, fieldMapFromFile[2].pos.toLong())
        assertEquals(3, fieldMapFromFile[3].pos.toLong())
    }

    @Test
    fun testFieldMapFromFile_noHeader_DoNotUseNumericFieldValues() {
        this.generator!!.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.AS_IS,
                        false, false, true, true))

        val fieldMapFromFile = this.generator!!.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0].javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1].javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2].javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3].javaFieldName)
        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[0].type)
        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[1].type)
        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[2].type)
        assertEquals("Field type=String when at least one field is a String",
                String::class.java!!.getSimpleName(), fieldMapFromFile[3].type)
        assertEquals(0, fieldMapFromFile[0].pos.toLong())
        assertEquals(1, fieldMapFromFile[1].pos.toLong())
        assertEquals(2, fieldMapFromFile[2].pos.toLong())
        assertEquals(3, fieldMapFromFile[3].pos.toLong())
    }

    @Test
    fun testFieldMapFromFile_noHeader_UseNumericFieldValues_PrimitiveTypes() {
        this.generator!!.setGeneratorConfig(
                getConfig(";", "Jonsson", FieldMapping.AS_IS,
                        false, true, true, true))

        val fieldMapFromFile = this.generator!!.fieldMapFromFile
        assertEquals("Size=4", 4, fieldMapFromFile.size.toLong())
        assertEquals("Field name=COLUMN_0", "COLUMN_0", fieldMapFromFile[0].javaFieldName)
        assertEquals("Field name=COLUMN_1", "COLUMN_1", fieldMapFromFile[1].javaFieldName)
        assertEquals("Field name=COLUMN_2", "COLUMN_2", fieldMapFromFile[2].javaFieldName)
        assertEquals("Field name=COLUMN_3", "COLUMN_3", fieldMapFromFile[3].javaFieldName)

        assertEquals("Field type=String", String::class.java!!.getSimpleName(), fieldMapFromFile[0].type)
        assertEquals("Field type=int", "int", fieldMapFromFile[1].type)
        assertEquals("Field type=float when mixed int and decimal types",
                "float", fieldMapFromFile[2].type)
        assertEquals("Field type=String when at least one field is a String",
                String::class.java!!.getSimpleName(), fieldMapFromFile[3].type)

        assertEquals(0, fieldMapFromFile[0].pos.toLong())
        assertEquals(1, fieldMapFromFile[1].pos.toLong())
        assertEquals(2, fieldMapFromFile[2].pos.toLong())
        assertEquals(3, fieldMapFromFile[3].pos.toLong())
    }

    @Test
    fun testGetHeaderField_LowerCamelCase() {
        val headerField = this.generator!!.getHeaderField("APEX_TWIN", FieldMapping.LOWER_CAMEL_CASE)
        assertEquals("should be APEX_TWIN", "APEX_TWIN", headerField.dataSourceName)
        assertEquals("Should now be apexTwin", "apexTwin", headerField.javaFieldName)
    }

    @Test
    fun testGetHeaderField_AsIs_QuoteCharsRemoved() {
        val headerField = this.generator!!.getHeaderField("'\"MY_MONKEY\"'", FieldMapping.AS_IS)
        assertEquals("Should be MY_MONKEY", "MY_MONKEY", headerField.dataSourceName)
        assertEquals("Should now be MY_MONKEY", "MY_MONKEY", headerField.javaFieldName)
    }

    @Test
    fun testGetHeaderField_LowerCamelCase_IllegalDelimitersRemoved() {
        val headerField = this.generator!!.getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.LOWER_CAMEL_CASE)
        assertEquals("Should be MY-LITTLE MONKEY", "MY-LITTLE MONKEY", headerField.dataSourceName)
        assertEquals("Should now be myLittleMonkey", "myLittleMonkey", headerField.javaFieldName)
    }

    @Test(expected = RuntimeException::class)
    fun testGetHeaderField_AsIs_IllegalDelimitersFound() {
        val headerField = this.generator!!.getHeaderField("MY-LITTLE MONKEY\"'", FieldMapping.AS_IS)
        assertNull("Header field could not be transformed", headerField.javaFieldName)
    }

    private fun getConfig(delimiter: String, javaClassName: String, fieldMapping: FieldMapping,
                          isHeader: Boolean, useNumericFieldTypes: Boolean,
                          usePrimitiveTypesWherePossible: Boolean,
                          includeColumnName: Boolean): GeneratorConfig {
        return GeneratorConfig(delimiter, javaClassName, fieldMapping, isHeader, useNumericFieldTypes, usePrimitiveTypesWherePossible, includeColumnName)
    }
}