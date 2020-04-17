/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package quarano.core;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Profile("!prod & !integrationtest")
@RequiredArgsConstructor
@Slf4j
@Component
public class DatabaseInitializer implements BeanPostProcessor {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.config.BeanPostProcessor#postProcessAfterInitialization(java.lang.Object, java.lang.String)
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

		if (!DataSource.class.isInstance(bean)) {
			return bean;
		}

		JdbcTemplate template = new JdbcTemplate((DataSource) bean);
		List<Map<String, Object>> result = template.queryForList("select tablename from pg_tables;");

		result.stream() //
				.map(it -> it.get("tablename").toString()) //
				.filter(it -> !it.startsWith("pg_") && !it.startsWith("sql_")) //
				.peek(it -> log.info("Dropping database table " + it)) //
				.forEach(it -> template.execute(String.format("DROP TABLE \"%s\" CASCADE;", it)));

		return bean;
	}

}
