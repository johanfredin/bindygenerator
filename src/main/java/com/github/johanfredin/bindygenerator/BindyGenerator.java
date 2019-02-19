package com.github.johanfredin.bindygenerator;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.text.WordUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Path;
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

            pw.println();
            pw.println("import org.apache.camel.dataformat.bindy.annotation.CsvRecord;");
            pw.println("import org.apache.camel.dataformat.bindy.annotation.DataField;");
            pw.println();
            pw.println("@CsvRecord(separator = \"" + generatorConfig.getDelimiter() + "\")");
            pw.println("public class " + generatorConfig.getJavaClassName() + " {");
            pw.println();
            // Begin iterating the fields
            for (BindyField field : fieldMapFromFile.values()) {
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
                if (generatorConfig.isUseNumericFieldTypes()) {
                    Set<FieldType> fieldTypesAtIndex = fieldTypesMap.get(index);
                    if (fieldTypesAtIndex == null) {
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
        if (generatorConfig.isUseNumericFieldTypes()) {
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
                .mapToObj(i -> generatorConfig.isHeader() ? getHeaderField(header[i], generatorConfig.getFieldMapping()) : "COLUMN_" + i)
                .toArray(String[]::new);
    }

    String getHeaderField(String headerField, FieldMapping fieldMapping) {
        // Start by removing quote chars (always do this)
        String result = headerField
                .trim()
                .replace("\"", "")
                .replace("'", "");

        if (fieldMapping == FieldMapping.AS_IS) {
            // When mapping=AS_IS then check for illegal delimiters
            List<Character> illegalDelimiters = Arrays.asList('.',' ', '-');
            for(Character c : result.toCharArray()) {
                if(illegalDelimiters.contains(c)) {
                    Character illegalCharacter = illegalDelimiters.get(illegalDelimiters.indexOf(c));
                    throw new RuntimeException(
                            "Illegal character=" + illegalCharacter + " found in field=" + headerField + ". This needs to be fixed");
                }
            }
            return result;
        }

        // Replace any separation chars to white space so we can capitalize
        result = result
                .toLowerCase()
                .replace(".", " ")
                .replace("_", " ")
                .replace("-", " ");

        // Now capitalize
        result = WordUtils.capitalize(result);

        // Now remove spaces
        result = result.replace(" ", "");

        // Now convert first character to lower case
        char[] chars = result.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    private FieldType getType(String column) {
        if (column != null && !column.isEmpty()) {
            column = column
                    .replace("\"", "")
                    .replace("'", "");
            if (NumberUtils.isParsable(column)) {
                String type;
                byte priority;
                boolean isPrimitiveTypes = this.generatorConfig.isUsePrimitiveTypesWherePossible();
                if (column.contains(".")) {
                    type = isPrimitiveTypes ? "float" : Float.class.getSimpleName();
                    priority = 2;
                } else {
                    type = isPrimitiveTypes ? "int" : Integer.class.getSimpleName();
                    priority = 3;
                }
                return new FieldType(priority, type);
            }
        }
        return new FieldType((byte) 1, String.class.getSimpleName());
    }
}
