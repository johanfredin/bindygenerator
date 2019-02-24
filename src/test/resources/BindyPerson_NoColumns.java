
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class Jonsson {

	@DataField(pos=1)
	private StringCompanionObject name;

	@DataField(pos=2)
	private StringCompanionObject age;

	@DataField(pos=3)
	private StringCompanionObject tax;

	@DataField(pos=4)
	private StringCompanionObject mixedBag;

}
