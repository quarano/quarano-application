package quarano.core;

import java.io.Writer;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.comparator.Comparators;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

/**
 * @author Jens Kutzsche
 * @since 1.4
 */
@Configuration
class CSVBeanMappingConfiguration {

	private static final char DEFAULT_SEPARATOR = ';';

	@Bean
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	<T> ICSVWriter csvWriter(@Nullable Writer writer) {

		return new CSVWriterBuilder(writer)
				.withSeparator(DEFAULT_SEPARATOR)
				.build();
	}

	@Bean(name = "fixMappingStrategyAndWriter")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	<T> StatefulBeanToCsv<T> beanToCsv(@Nullable Writer writer, @NonNull Class<T> type,
			@Nullable List<String> columnOrder) {

		return doBeanToCsv(writer, mappingStrategy(type, columnOrder));
	}

	@Bean(name = "givenMappingStrategyAndWriter")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	<T> StatefulBeanToCsv<T> beanToCsv(@Nullable Writer writer, @Nullable MappingStrategy<T> mappingStrategy) {

		return doBeanToCsv(writer, mappingStrategy);
	}

	@Bean(name = "fixMappingStrategyAndCsvWriter")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	<T> StatefulBeanToCsv<T> beanToCsv(@Nullable ICSVWriter writer, @NonNull Class<T> type,
			@Nullable List<String> columnOrder) {

		return doBeanToCsv(writer, mappingStrategy(type, columnOrder));
	}

	@Bean(name = "givenMappingStrategyAndCsvWriter")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	<T> StatefulBeanToCsv<T> beanToCsv(@Nullable ICSVWriter writer, @Nullable MappingStrategy<T> mappingStrategy) {

		return doBeanToCsv(writer, mappingStrategy);
	}

	@Bean(name = "comparatorForOrderArray")
	@Scope(BeanDefinition.SCOPE_PROTOTYPE)
	Comparator<String> createOrderComparatorFor(@Nullable List<String> orderArray) {

		List<String> predefinedOrder = orderArray == null ? List.of() : orderArray;

		var fixedComparator = new FixedOrderComparator<>(predefinedOrder);
		fixedComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

		return fixedComparator.thenComparing(Comparators.nullsLow()).thenComparing(String.CASE_INSENSITIVE_ORDER);
	}

	private <T> StatefulBeanToCsv<T> doBeanToCsv(@Nullable Writer writer,
			@Nullable MappingStrategy<T> mappingStrategy) {

		return configureBeanToCsv(new StatefulBeanToCsvBuilder<T>(writer), mappingStrategy);
	}

	private <T> StatefulBeanToCsv<T> doBeanToCsv(@Nullable ICSVWriter writer,
			@Nullable MappingStrategy<T> mappingStrategy) {

		return configureBeanToCsv(new StatefulBeanToCsvBuilder<T>(writer), mappingStrategy);
	}

	private <T> StatefulBeanToCsv<T> configureBeanToCsv(StatefulBeanToCsvBuilder<T> beanToCsvBuilder,
			MappingStrategy<T> mappingStrategy) {

		return beanToCsvBuilder
				.withMappingStrategy(mappingStrategy)
				.withSeparator(DEFAULT_SEPARATOR)
				.withErrorLocale(LocaleContextHolder.getLocale())
				.build();
	}

	private <T> MappingStrategy<T> mappingStrategy(@NonNull Class<T> type, @Nullable List<String> columnOrder) {

		Assert.notNull(type, "Type must not be null!");

		var mappingStrategy = new HeaderColumnNameMappingStrategy<T>();
		mappingStrategy.setType(type);

		if (columnOrder != null) {
			mappingStrategy.setColumnOrderOnWrite(createOrderComparatorFor(columnOrder));
		}

		return mappingStrategy;
	}
}
