package quarano.occasion.web;

import quarano.core.web.MappingCustomizer;
import quarano.occasion.Occasion;
import quarano.occasion.OccasionCode;
import quarano.occasion.Visitor;
import quarano.occasion.VisitorGroup;
import quarano.occasion.web.OccasionRepresentions.VisitorDto;
import quarano.occasion.web.OccasionRepresentions.VisitorGroupDto;
import quarano.tracking.Address;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

/**
 * DTO mapping configuration for {@link Occasion}s.
 *
 * @author Oliver Drotbohm
 */
@Component
class OccasionMappingConfiguration implements MappingCustomizer {

	/*
	 * (non-Javadoc)
	 * @see quarano.core.web.MappingCustomizer#customize(org.modelmapper.ModelMapper)
	 */
	@Override
	public void customize(ModelMapper mapper) {

		mapper.typeMap(VisitorDto.class, Visitor.class).setProvider(ctx -> {

			var source = (VisitorDto) ctx.getSource();
			var address = mapper.map(source, Address.class);

			return new Visitor(source.getFirstName(), source.getLastName(), address);
		});

		mapper.typeMap(VisitorGroupDto.class, VisitorGroup.class).setProvider(ctx -> {

			var source = (VisitorGroupDto) ctx.getSource();

			return new VisitorGroup(source.getEnd(), OccasionCode.of(source.getEventCode()));
		});
	}
}
