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
 * @author Oliver Drotbohm
 */
@Component
@RequiredArgsConstructor
@Order(200)
@Slf4j
public class AccountBootstrap implements ApplicationRunner {

	private final DepartmentRepository departments;
	private final AccountService accounts;
	private final DepartmentProperties configuration;
	private final @NonNull RoleRepository roleRepository;

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.ApplicationRunner#run(org.springframework.boot.ApplicationArguments)
	 */
	@Override
	@Transactional
	public void run(ApplicationArguments args) throws Exception {

		Department defaultDepartment = configuration.getDefaultDepartment();
		if (departments.count() > 0) {

			log.info("Found departments in database. Updating their contact informations");

			String departmentName = defaultDepartment.getName();
			departments.findByName(departmentName)
					.map(department -> {
						// workaround for hibernate as orphanremoval is not working here
						departments.deleteDepartmentContacts(department.getContacts());
						return department;
					})
					.map(department -> department.addDepartmentContacts(defaultDepartment.getContacts()))
					.ifPresent(departments::save);

			return;
		}

		log.info("Creating default department.");

		var department = departments.save(defaultDepartment);
		var defaults = configuration.getDefaultAccount();


		// create initial roles
		for (RoleType type : RoleType.values()) {

			var role = roleRepository.findByName(type.getCode());

			if (role != null) {
				continue;
			}
			log.info("Creating initial role " + type.getCode());
			
			roleRepository.save(new Role(type));
		}

		

		log.info("Creating default account (root, root).");

		accounts.createStaffAccount("root", UnencryptedPassword.of("root"), //
				defaults.getFirstname(), //
				defaults.getLastname(), //
				EmailAddress.of(defaults.getEmailAddress()), //
				department.getId(), RoleType.ROLE_HD_ADMIN);
		
	}
}
