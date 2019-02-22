package com.github.johanfredin.bindygenerator

import java.nio.file.Paths

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        val config = GeneratorConfig(
                "\t",
                "BPDto",
                FieldMapping.LOWER_CAMEL_CASE,
                true,
                true,
                true,
                true
        )

        val dataSourcePath = Paths.get("src/main/resources/BP.txt")
        val javaSourcePath = Paths.get("src/main/resources/" + config.javaClassName + ".java")

        val generator = BindyGenerator(config, dataSourcePath, javaSourcePath)
        generator.generate()
    }
}
