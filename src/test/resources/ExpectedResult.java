
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class BindyPerson {

	@DataField(pos=1)
	private String name;

	@DataField(pos=2)
	private int age;

	@DataField(pos=3)
	private float tax;

	@DataField(pos=4)
	private String mixedBag;

}
