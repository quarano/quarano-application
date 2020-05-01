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
package quarano.reference;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

@Entity
@Table(name = "symptoms")
@AllArgsConstructor
@Getter
@Setter(value = AccessLevel.PACKAGE)
public class Symptom implements Persistable<UUID> {

	@Id //
	private UUID id;
	private @Transient boolean isNew = true;
	@Column(name = "symptom_name")
	private String name;
	private boolean isCharacteristic;

	Symptom() {
		this.id = UUID.randomUUID();
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.data.domain.Persistable#isNew()
	 */
	@Override
	public boolean isNew() {
		return isNew;
	}

	@PrePersist
	@PostLoad
	void markNotNew() {
		this.isNew = false;
	}
}
