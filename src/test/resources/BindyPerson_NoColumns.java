
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class Jonsson {

	@DataField(pos=1)
	private String name;

	@DataField(pos=2)
	private String age;

	@DataField(pos=3)
	private Float tax;

	@DataField(pos=4)
	private String mixedBag;

}
