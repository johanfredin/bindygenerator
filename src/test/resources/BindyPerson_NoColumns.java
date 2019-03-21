
import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField;

@CsvRecord(separator = ";")
public class Jonsson {

	@DataField(pos=1, columnName="COLUMN_0")
	private String COLUMN_0;

	@DataField(pos=2, columnName="COLUMN_1")
	private Integer COLUMN_1;

	@DataField(pos=3, columnName="COLUMN_2")
	private Float COLUMN_2;

	@DataField(pos=4, columnName="COLUMN_3")
	private String COLUMN_3;

}
