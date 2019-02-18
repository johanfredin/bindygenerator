package=com.github.johanfredin.bindygenerator

import org.apache.camel.dataformat.bindy.annotation.CsvRecord;
import org.apache.camel.dataformat.bindy.annotation.DataField

@CsvRecord(separator = ";")
public class BindyPerson {

	@DataField(pos=1)
	private String name;

	@DataField(pos=2)
	private Integer age;

	@DataField(pos=3)
	private Double tax;

	@DataField(pos=4)
	private String mixed-bag;

}
