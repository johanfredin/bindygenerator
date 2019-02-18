package com.github.johanfredin.bindygenerator;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.IntStream;

class BindyGenerator {

    private GeneratorConfig generatorConfig;
    private File file;
    private Path javaSourceFilePath;

    BindyGenerator(GeneratorConfig generatorConfig, File sourceFile, Path javaSourceFilePath) {
        this.generatorConfig = generatorConfig;
        this.file = sourceFile;
        this.javaSourceFilePath = javaSourceFilePath;
    }

    void setGeneratorConfig(GeneratorConfig generatorConfig) {
        this.generatorConfig = generatorConfig;
    }

    void generate() {
        Map<Integer, BindyField> fieldMapFromFile = getFieldMapFromFile();
        PrintWriter pw = null;
        try {
            File javaSourceFile = this.javaSourceFilePath.toFile();
            pw = new PrintWriter(javaSourceFile);
            assert pw != null;

            pw.println("package=" + generatorConfig.getPackageName());
            pw.println();
            pw.println("import org.apache.camel.dataformat.bindy.annotation.CsvRecord;");
            pw.println("import org.apache.camel.dataformat.bindy.annotation.DataField;");
            pw.println();
            pw.println("@CsvRecord(separator = \"" + generatorConfig.getDelimiter() + "\")");
            pw.println("public class " + generatorConfig.getJavaClassName() + " {");
            pw.println();
            // Begin iterating the fields
            for(BindyField field : fieldMapFromFile.values()) {
                pw.println("\t@DataField(pos=" + (field.getPos() + 1) + ")"); // Bindy field positions start at 1!
                pw.println("\tprivate " + field.getType() + " " + field.getName() + ";");
                pw.println();
            }
            pw.println("}");
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }

    Map<Integer, BindyField> getFieldMapFromFile() {
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        assert sc != null;
        String[] header = getHeader(sc.nextLine());
        Map<Integer, BindyField> bindyFieldMap = new HashMap<>();
        Map<Integer, Set<FieldType>> fieldTypesMap = new HashMap<>();

        // Fetch the context from the file and decide field types.
        while (sc.hasNextLine()) {
            String currentLine = sc.nextLine();
            String[] record = currentLine.split(generatorConfig.getDelimiter());
            int index = 0;
            for (String column : record) {
                BindyField bindyField = bindyFieldMap.get(index);
                if (bindyField == null) {
                    bindyField = new BindyField();
                    bindyField.setName(header[index]);
                    bindyField.setPos(index);
                }

                // Find all possible field types for current field
                if(generatorConfig.isUseNumericFieldTypes()) {
                    Set<FieldType> fieldTypesAtIndex = fieldTypesMap.get(index);
                    if(fieldTypesAtIndex == null) {
                        fieldTypesAtIndex = new HashSet<>();
                    }
                    fieldTypesAtIndex.add(getType(column));
                    fieldTypesMap.put(index, fieldTypesAtIndex);
                }
                bindyFieldMap.put(index, bindyField);
                index++;
            }
        }

        // Now set the highest priority field for the BindyFieldsMap
        if(generatorConfig.isUseNumericFieldTypes()) {
            bindyFieldMap.values()
                    .forEach(e -> {
                        BindyField bindyField = bindyFieldMap.get(e.getPos());
                        Set<FieldType> fieldTypes = fieldTypesMap.get(e.getPos());
                        Optional<FieldType> first = fieldTypes
                                .stream()
                                .sorted()
                                .findFirst();
                        bindyField.setType(first.orElseThrow(RuntimeException::new).getType());
                    });
        }
        return bindyFieldMap;
    }

    private String[] getHeader(String nextLine) {
        final String[] header = nextLine.split(generatorConfig.getDelimiter());
        return IntStream.range(0, header.length)
                .mapToObj(i -> generatorConfig.isHeader() ? header[i] : "COLUMN_" + i)
                .toArray(String[]::new);
    }

    private FieldType getType(String column) {
        if (column != null && !column.isEmpty()) {
            if (NumberUtils.isParsable(column)) {
                if (column.contains(".")) {
                    return new FieldType((byte) 2, Double.class.getSimpleName());
                }
                return new FieldType((byte) 3, Integer.class.getSimpleName());
            }
        }
        return new FieldType((byte) 1, String.class.getSimpleName());
    }
}
