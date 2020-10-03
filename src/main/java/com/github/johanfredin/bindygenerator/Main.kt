package com.github.johanfredin.bindygenerator

import java.nio.file.Paths

fun main() {
    val config = GeneratorConfig(
            delimiter = "\t",
            javaClassName = "",
            fieldMapping = FieldMapping.LOWER_CAMEL_CASE,
            isHeader = true,
            isIncludeColumnName = true,
            decimalType = DecimalType.BIG_DECIMAL,
            integerType = IntegerType.BYTE,
            stringType = StringType.STRING
    )

    val dataSourcePath = Paths.get("src/main/resources/.txt")
    val javaSourcePath = Paths.get("src/main/resources/" + config.javaClassName + ".java")

    val generator = BindyGenerator(config, dataSourcePath, javaSourcePath)
    generator.generate()
}
