package quarano.sormas_integration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

import org.h2.api.Trigger;

public class ChangesForSyncTrigger implements Trigger {

	private String tableName;

	@Override
	public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type)
			throws SQLException {

		this.tableName = tableName;
	}

	@Override
	public void fire(Connection conn, Object[] oldRow, Object[] newRow) throws SQLException {

		try (PreparedStatement stmt = conn.prepareStatement(
				"INSERT INTO changes_for_sync (id, created, source_table, ref_id) " +
						"VALUES (?, ?, ?, ?)")) {

			stmt.setObject(1, UUID.randomUUID());
			stmt.setObject(2, LocalDateTime.now());
			stmt.setObject(3, tableName);
			stmt.setObject(4, newRow[0]);
			stmt.executeUpdate();
		}

		// var templates = new H2Templates(); // SQL-dialect
		// var configuration = new Configuration(templates);
		// var query = new SQLQuery(conn, configuration);
		//
		// List<String> lastNames = query.from(Qtra)
		// .where(customer.firstName.eq("Bob"))
		// .list(customer.lastName);
	}

	@Override
	public void close() throws SQLException {}

	@Override
	public void remove() throws SQLException {}
}
