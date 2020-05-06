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
package quarano.account;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A masterdata-entity, which can be assigned to an account.
 *
 * @author Patrick Otto
 * @author Michael J. Simons
 */
@Entity
@Table(name = "roles")
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Role {

	@Id //
	@GeneratedValue(strategy = GenerationType.IDENTITY) //
	@Column(name = "role_id") //
	private int id;

	@Column(name = "role_name") //
	private String name;

	public Role(RoleType name) {
		this.name = name.getCode();
	}

	public Role(RoleType name, int id) {
		this.name = name.getCode();
	}

	@Override
	public String toString() {
		return this.name;
	}

	public static Role of(RoleType type) {
		return new Role(type);
	}
	
	public RoleType getRoleType() {
		return RoleType.valueOf(this.name);
	}

}
