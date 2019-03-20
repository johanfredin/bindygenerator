package com.github.johanfredin.bindygenerator

import org.apache.commons.lang3.math.NumberUtils
import org.apache.commons.text.WordUtils
import java.io.FileNotFoundException
import java.io.PrintWriter
import java.nio.file.Path
import java.util.*
import java.util.logging.Logger
import kotlin.collections.ArrayList

/**
 * Main class that uses all the other enums and data classes to map an input file
 * to a bindy mapped java class.
 * @property generatorConfig the configuration to use
 * @property pathToDataSource the path to the input data source
 * @property javaSourceFilePath where the resulting java source file will end up.
 */
internal class BindyGenerator(private var generatorConfig: GeneratorConfig,
                              private val pathToDataSource: Path,
                              private var javaSourceFilePath: Path) {

    private val log: Logger = Logger.getLogger(javaClass.name)

    /**
     * Fetch the context from the file and decide field types.
     * Find all possible field types for current field
     * Now set the highest priority field for the BindyFieldsMap
     */
    val fieldMapFromFile: Map<Int, BindyField>
        get() {
            val sc = Scanner(pathToDataSource.toFile())
            log.info("Parsing file at $pathToDataSource")
            val header = getHeader(sc!!.nextLine())
            val bindyFieldMap = HashMap<Int, BindyField>()
            val fieldTypesMap = HashMap<Int, MutableSet<FieldType>>()

            while (sc.hasNextLine()) {

                val currentLine = sc.nextLine()
                val record = currentLine.split(this.generatorConfig.delimiter.toRegex()).dropLastWhile(String::isEmpty).toTypedArray()
                var index = 0

                record.forEach { column ->
                    var bindyField = bindyFieldMap[index]
                    if (bindyField == null) {
                        bindyField = BindyField(index, header[index].dataSourceName, header[index].javaFieldName)
                    }
                    var fieldTypesAtIndex: MutableSet<FieldType>? = fieldTypesMap[index]
                    if (fieldTypesAtIndex == null) {
                        fieldTypesAtIndex = HashSet()
                    }
                    fieldTypesAtIndex.add(getType(column))
                    fieldTypesMap[index] = fieldTypesAtIndex
                    bindyFieldMap[index] = bindyField
                    index++
                }
            }
            bindyFieldMap.values.forEach { e ->
                val bindyField = bindyFieldMap[e.pos]
                val fieldTypes = fieldTypesMap[e.pos]

                var type = FieldType.STRING.objectTypeName
                if (fieldTypes != null) {
                    val first = fieldTypes
                            .stream()
                            .sorted(Comparator.comparing(FieldType::priority))
                            .findFirst()

                    val optionalType = first?.orElseThrow<RuntimeException>(::RuntimeException)
                    type = optionalType?.objectTypeName!!
                }

                bindyField?.type = type
            }
            return bindyFieldMap
        }

    fun generate() {
        log.info("Selected configuration=$generatorConfig")
        val fieldMapFromFile = this.fieldMapFromFile
        var pw: PrintWriter? = null
        try {
            val javaSourceFile = this.javaSourceFilePath.toFile()
            pw = PrintWriter(javaSourceFile)

            pw.println()
            pw.println("import org.apache.camel.dataformat.bindy.annotation.CsvRecord;")
            pw.println("import org.apache.camel.dataformat.bindy.annotation.DataField;")
            pw.println()
            pw.println("@CsvRecord(separator = \"" + generatorConfig.delimiter + "\")")
            pw.println("public class " + generatorConfig.javaClassName + " {")
            pw.println()
            // Begin iterating the fields
            for (field in fieldMapFromFile.values) {
                pw.print("\t@DataField(pos=" + (field.pos + 1)) // Bindy field positions start at 1!
                if (generatorConfig.isIncludeColumnName) {
                    pw.print(", columnName=\"" + field.dataSourceName + "\")")
                } else {
                    pw.print(")")
                }

                pw.println()
                pw.println("\tprivate " + field.type + " " + field.javaFieldName + ";")
                pw.println()
                log.info("Field added, type=${field.type}, name=${field.javaFieldName}")
            }
            pw.println("}")
            log.info("java source file $javaSourceFile created")
        } catch (ex: Exception) {
            ex.printStackTrace()
        } finally {
            pw?.close()
        }
    }

    private fun getHeader(nextLine: String): List<DatasetHeader> {
        val header = nextLine.split(this.generatorConfig.delimiter.toRegex()).dropLastWhile(String::isEmpty)
        val list = ArrayList<DatasetHeader>()
        header.withIndex().forEach { (i, s) ->
            if (this.generatorConfig.isHeader) {
                list.add(getHeaderField(s, this.generatorConfig.fieldMapping))
            } else {
                list.add(DatasetHeader("COLUMN_$i", "COLUMN_$i"))
            }
        }
        log.info("Header row=${Arrays.toString(list.toArray())}")
        return list
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

    fun getType(column: String?): FieldType {
        var mutableColumn = column
        if (mutableColumn != null && !mutableColumn.isEmpty()) {
            mutableColumn = mutableColumn
                    .replace("\"", "")
                    .replace("'", "")
            when {
                NumberUtils.isParsable(mutableColumn) -> {
                    return if (mutableColumn.contains(".")) {
                        FieldType.FLOAT
                    } else FieldType.INTEGER
                }
            }
        }
        return FieldType.STRING
    }
}
