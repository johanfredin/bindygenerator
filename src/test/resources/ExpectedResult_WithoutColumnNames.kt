import org.apache.camel.dataformat.bindy.annotation.CsvRecord
import org.apache.camel.dataformat.bindy.annotation.DataField

@CsvRecord(separator = ";")
class Jonsson {

    @DataField(pos = 1)
    private val name: String? = null

    @DataField(pos = 2)
    private val age: Int? = null

    @DataField(pos = 3)
    private val tax: Float? = null

    @DataField(pos = 4)
    private val mixedBag: String? = null

}
