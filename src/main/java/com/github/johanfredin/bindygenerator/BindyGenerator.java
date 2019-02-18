package com.github.johanfredin.bindygenerator;

import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.IntStream;

public class BindyGenerator {

    private GeneratorConfig generatorConfig;
    private File file;

    public BindyGenerator(GeneratorConfig generatorConfig, File file) {
        this.generatorConfig = generatorConfig;
        this.file = file;
    }

    public File generate() {
        return null;
    }

    public Map<Integer, BindyField> getFieldMapFromFile() {
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
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
                        bindyField.setType(first.get().getType());
                    });

            bindyFieldMap.entrySet().forEach(System.out::println);
        }
        return bindyFieldMap;
    }

    public String[] getHeader(String nextLine) {
        final String[] header = nextLine.split(generatorConfig.getDelimiter());
        return IntStream.range(0, header.length)
                .mapToObj(i -> generatorConfig.isHeader() ? header[i] : "COLUMN_" + i)
                .toArray(String[]::new);
    }

    public FieldType getType(String column) {
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
