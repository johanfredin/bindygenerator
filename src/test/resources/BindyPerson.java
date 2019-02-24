
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class BindyPerson {

	@DataField(pos=1, columnName="NAME")
	private StringCompanionObject name;

	@DataField(pos=2, columnName="AGE")
	private StringCompanionObject age;

	@DataField(pos=3, columnName="TAX")
	private StringCompanionObject tax;

	@DataField(pos=4, columnName="MIXED_BAG")
	private StringCompanionObject mixedBag;

}
