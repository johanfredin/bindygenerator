package com.github.johanfredin.bindygenerator

enum class DecimalType(val label: String) {

    FLOAT("Float"),
    FLOAT_PRIMITIVE("float"),
    DOUBLE("Double"),
    DOUBLE_PRIMITIVE("double"),
    BIG_DECIMAL("BigDecimal"),
    DEFAULT("String")
}