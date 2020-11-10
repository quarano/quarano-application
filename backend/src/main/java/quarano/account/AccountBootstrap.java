package quarano.account;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import quarano.account.Password.UnencryptedPassword;
import quarano.core.EmailAddress;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * An {@link ApplicationRunner} that makes sure
 *
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(200)
@Slf4j
public class AccountBootstrap implements ApplicationRunner {

	private final @NonNull DepartmentRepository departments;
	private final @NonNull AccountService accounts;
	private final @NonNull DepartmentProperties configuration;
	private final @NonNull RoleRepository roles;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {

		Department defaultDepartment = configuration.getDefaultDepartment();

		if (departments.count() > 0) {

			log.info("Found departments in database. Updating their contact information and RKI code.");

			departments.findByName(defaultDepartment.getName())
					.map(department -> department.setContacts(defaultDepartment.getContacts()))
					.map(department -> department.setRkiCode(defaultDepartment.getRkiCode()))
					.ifPresent(departments::save);

			return;
		}

		log.info("Creating default department.");

		var department = departments.save(defaultDepartment);
		var defaults = configuration.getDefaultAccount();

		// Create initial roles
		for (RoleType type : RoleType.values()) {

			roles.findByName(type.getCode()).orElseGet(() -> {

				log.info("Adding initial role " + type);
				return roles.save(new Role(type));
			});
		}

		log.info("Creating default account (root, root).");

		var password = UnencryptedPassword.of("root");
		var account = accounts.createStaffAccount("root", password,
				defaults.getFirstname(),
				defaults.getLastname(),
				EmailAddress.of(defaults.getEmailAddress()),
				department.getId(), RoleType.ROLE_HD_ADMIN);

		accounts.changePassword(password, account);
	}
}
