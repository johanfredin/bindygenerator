package com.github.johanfredin.bindygenerator;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String... args) {
        GeneratorConfig config = new GeneratorConfig(
                "\t",
                "Foo",
                FieldMapping.LOWER_CAMEL_CASE,
                true,
                true,
                true,
                true
        );

        Path dataSourcePath = Paths.get("src/main/resources/foo.csv");
        Path javaSourcePath = Paths.get("src/main/resources/" + config.getJavaClassName() + ".java");

        BindyGenerator generator = new BindyGenerator(config, dataSourcePath, javaSourcePath);
        generator.generate();
    }
}
