package com.github.johanfredin.bindygenerator

import java.nio.file.Paths

fun main() {
    val config = GeneratorConfig(
            "\t",
            "BPDto",
            FieldMapping.LOWER_CAMEL_CASE,
            isHeader = true,
            isUseNumericFieldTypes = true,
            isUsePrimitiveTypesWherePossible = true,
            isIncludeColumnName = true
    )

    val dataSourcePath = Paths.get("src/main/resources/BP.txt")
    val javaSourcePath = Paths.get("src/main/resources/" + config.javaClassName + ".java")

    val generator = BindyGenerator(config, dataSourcePath, javaSourcePath)
    generator.generate()
}