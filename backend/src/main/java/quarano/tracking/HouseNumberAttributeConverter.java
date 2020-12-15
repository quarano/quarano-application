package quarano.tracking;

import quarano.core.Address.HouseNumber;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author Oliver Drotbohm
 */
@Converter(autoApply = true)
class HouseNumberAttributeConverter implements AttributeConverter<HouseNumber, String> {

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToDatabaseColumn(java.lang.Object)
	 */
	@Override
	public String convertToDatabaseColumn(HouseNumber number) {
		return number == null || HouseNumber.NONE.equals(number) ? null : number.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see javax.persistence.AttributeConverter#convertToEntityAttribute(java.lang.Object)
	 */
	@Override
	public HouseNumber convertToEntityAttribute(String source) {
		return source == null ? HouseNumber.NONE : HouseNumber.of(source);
	}
}
