package quarano.core;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Embeddable;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Oliver Drotbohm
 */
@Getter
@Embeddable
public class AuditingMetadata {

	@LastModifiedDate LocalDateTime lastModified;
	@LastModifiedBy UUID lastModifiedBy;
	@CreatedDate LocalDateTime created;
	@CreatedBy UUID createdBy;
}
