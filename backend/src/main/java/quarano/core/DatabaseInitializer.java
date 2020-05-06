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

import javax.sql.DataSource;

import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Component;

/**
 * @author Oliver Drotbohm
 */
@Profile("develop")
@RequiredArgsConstructor
@Slf4j
@Component
public class DatabaseInitializer implements FlywayConfigurationCustomizer {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer#customize(org.flywaydb.core.api.configuration.FluentConfiguration)
	 */
	@Override
	public void customize(FluentConfiguration configuration) {

		DataSource dataSource = configuration.getDataSource();

		String url;
		try {
			url = JdbcUtils.extractDatabaseMetaData(dataSource, "getURL");

			if (!url.contains("postgres")) {
				log.info("Database is not a Postgres! Skipping data wiping!");
				return;
			}

		} catch (MetaDataAccessException e) {}

		log.info("Wiping database tables.");

		var template = new JdbcTemplate(dataSource);

		template.queryForList("select tablename from pg_tables;").stream() //
				.map(it -> it.get("tablename").toString()) //
				.filter(it -> !it.startsWith("pg_") && !it.startsWith("sql_")) //
				.peek(it -> log.info("Dropping database table " + it)) //
				.forEach(it -> template.execute(String.format("DROP TABLE \"%s\" CASCADE;", it)));
	}
}
