package com.github.johanfredin.bindygenerator

import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.text.WordUtils

import java.io.File
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.nio.file.Path
import java.util.*
import java.util.stream.IntStream

internal class BindyGenerator(private var generatorConfig: GeneratorConfig?, private val pathToDataSource: Path, private var javaSourceFilePath: Path?) {

    // Fetch the context from the file and decide field types.
    // Find all possible field types for current field
    // Now set the highest priority field for the BindyFieldsMap
    val fieldMapFromFile: Map<Int, BindyField>
        get() {
            var sc: Scanner? = null
            try {
                sc = Scanner(pathToDataSource.toFile())
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            assert(sc != null)
            val header = getHeader(sc!!.nextLine())
            val bindyFieldMap = HashMap<Int, BindyField>()
            val fieldTypesMap = HashMap<Int, Set<FieldType>>()
            while (sc.hasNextLine()) {
                val currentLine = sc.nextLine()
                val record = currentLine.split(generatorConfig!!.delimiter.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                var index = 0
                for (column in record) {
                    var bindyField: BindyField? = bindyFieldMap[index]
                    if (bindyField == null) {
                        bindyField = BindyField()
                        bindyField.dataSourceName = header[index].dataSourceName
                        bindyField.javaFieldName = header[index].javaFieldName
                        bindyField.pos = index
                    }
                    if (generatorConfig!!.isUseNumericFieldTypes) {
                        var fieldTypesAtIndex: MutableSet<FieldType>? = fieldTypesMap[index]
                        if (fieldTypesAtIndex == null) {
                            fieldTypesAtIndex = HashSet()
                        }
                        fieldTypesAtIndex.add(getType(column))
                        fieldTypesMap[index] = fieldTypesAtIndex
                    }
                    bindyFieldMap[index] = bindyField
                    index++
                }
            }
            bindyFieldMap.values
                    .forEach { e ->
                        val bindyField = bindyFieldMap[e.pos]
                        val fieldTypes = fieldTypesMap[e.pos]
                        if (generatorConfig!!.isUseNumericFieldTypes) {
                            val first = fieldTypes
                                    .stream()
                                    .sorted()
                                    .findFirst()
                            bindyField.type = first.orElseThrow<RuntimeException>(Supplier<RuntimeException> { RuntimeException() }).type
                        } else {
                            bindyField.type = String::class.java!!.getSimpleName()
                        }
                    }
            return bindyFieldMap
        }

    fun setGeneratorConfig(generatorConfig: GeneratorConfig) {
        this.generatorConfig = generatorConfig
    }

    fun setJavaSourceFilePath(javaSourceFilePath: Path) {
        this.javaSourceFilePath = javaSourceFilePath
    }

    fun generate() {
        val fieldMapFromFile = fieldMapFromFile
        var pw: PrintWriter? = null
        try {
            val javaSourceFile = this.javaSourceFilePath!!.toFile()
            pw = PrintWriter(javaSourceFile)

            pw.println()
            pw.println("import org.apache.camel.dataformat.bindy.annotation.CsvRecord;")
            pw.println("import org.apache.camel.dataformat.bindy.annotation.DataField;")
            pw.println()
            pw.println("@CsvRecord(separator = \"" + generatorConfig!!.delimiter + "\")")
            pw.println("public class " + generatorConfig!!.javaClassName + " {")
            pw.println()
            // Begin iterating the fields
            for (field in fieldMapFromFile.values) {
                pw.print("\t@DataField(pos=" + (field.pos + 1)) // Bindy field positions start at 1!
                if (generatorConfig!!.isIncludeColumnName) {
                    pw.print(", columnName=\"" + field.dataSourceName + "\")")
                } else {
                    pw.print(")")
                }
                pw.println()
                pw.println("\tprivate " + field.type + " " + field.javaFieldName + ";")
                pw.println()
            }
            pw.println("}")
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            pw?.close()
        }
    }

    private fun getHeader(nextLine: String): Array<DatasetHeader> {
        val header = nextLine.split(generatorConfig!!.delimiter.toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
        return IntStream.range(0, header.size)
                .mapToObj { i ->
                    if (generatorConfig!!.isHeader)
                        getHeaderField(header[i], generatorConfig!!.fieldMapping)
                    else
                        DatasetHeader("COLUMN_$i", "COLUMN_$i")
                }
                .toArray(DatasetHeader[]::new  /* Currently unsupported in Kotlin */)
    }

    fun getHeaderField(headerField: String, fieldMapping: FieldMapping): DatasetHeader {
        // Start by removing quote chars (always do this)
        val cleanedHeaderField = headerField
                .trim { it <= ' ' }
                .replace("\"", "")
                .replace("'", "")

        if (fieldMapping == FieldMapping.AS_IS) {
            // When mapping=AS_IS then check for illegal delimiters
            val illegalDelimiters = Arrays.asList('.', ' ', '-')
            for (c in cleanedHeaderField.toCharArray()) {
                if (illegalDelimiters.contains(c)) {
                    val illegalCharacter = illegalDelimiters[illegalDelimiters.indexOf(c)]
                    throw RuntimeException(
                            "Illegal character=$illegalCharacter found in field=$headerField. This needs to be fixed")
                }
            }
            // Make datasource name and java field name the same when mapping=AS_IS
            return DatasetHeader(cleanedHeaderField, cleanedHeaderField)
        }

        // Replace any separation chars to white space so we can capitalize
        var result = cleanedHeaderField
                .toLowerCase()
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ")

        // Now capitalize
        result = WordUtils.capitalize(result)

        // Now remove spaces
        result = result.replace(" ", "")

        // Now convert first character to lower case
        val chars = result.toCharArray()
        chars[0] = Character.toLowerCase(chars[0])
        val lowerCamelCaseField = String(chars)
        return DatasetHeader(cleanedHeaderField, lowerCamelCaseField)
    }

    private fun getType(column: String?): FieldType {
        var column = column
        if (column != null && !column.isEmpty()) {
            column = column
                    .replace("\"", "")
                    .replace("'", "")
            if (NumberUtils.isParsable(column)) {
                val type: String
                val priority: Byte
                val isPrimitiveTypes = this.generatorConfig!!.isUsePrimitiveTypesWherePossible
                if (column!!.contains(".")) {
                    type = if (isPrimitiveTypes) "float" else Float::class.java!!.getSimpleName()
                    priority = 2
                } else {
                    type = if (isPrimitiveTypes) "int" else Int::class.java!!.getSimpleName()
                    priority = 3
                }
                return FieldType(priority.toInt(), type)
            }
        }
        return FieldType(1, String::class.java!!.getSimpleName())
    }
}
