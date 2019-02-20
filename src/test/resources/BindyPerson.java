
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class BindyPerson {

	@DataField(pos=1, columnName="NAME")
	private String name;

	@DataField(pos=2, columnName="AGE")
	private String age;

	@DataField(pos=3, columnName="TAX")
	private float tax;

	@DataField(pos=4, columnName="MIXED_BAG")
	private String mixedBag;

}
