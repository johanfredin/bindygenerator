import org.apache.camel.dataformat.bindy.annotation.CsvRecord
import org.apache.camel.dataformat.bindy.annotation.DataField

@CsvRecord(separator = ";")
class BindyPerson {

    @DataField(pos = 1, columnName = "NAME")
    private val name: String? = null

    @DataField(pos = 2, columnName = "AGE")
    private val age: Int = 0

    @DataField(pos = 3, columnName = "TAX")
    private val tax: Float = 0.toFloat()

    @DataField(pos = 4, columnName = "MIXED_BAG")
    private val mixedBag: String? = null

}
