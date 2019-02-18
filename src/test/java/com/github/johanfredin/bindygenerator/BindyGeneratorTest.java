package com.github.johanfredin.bindygenerator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BindyGeneratorTest {

    @Test
    public void testGenerate() {
        GeneratorConfig config = new GeneratorConfig();
        config.setDelimiter(";");
        config.setUseNumericFieldTypes(true);

        File file = Paths.get("src/test/resources/person.csv").toFile();

        File javaSourceFile = new BindyGenerator(config, file).generate();
        File expectedResult = Paths.get("src/test/resources/BindyPerson.java").toFile();

        assertEquals("Generated java file same as expected java file", javaSourceFile, expectedResult);
    }

    @Test
    public void getFieldMapFromFile() {
    }

    @Test
    public void getHeader() {
    }

    @Test
    public void getType() {
    }
}