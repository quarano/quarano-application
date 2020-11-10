package quarano.department;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import quarano.core.QuaranoEntity;
import quarano.department.Comment.CommentIdentifier;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.UUID;

import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jmolecules.ddd.types.Identifier;
import org.springframework.util.Assert;

/**
 * @author Oliver Drotbohm
 * @author Michael J. Simons
 */
@Entity
@Table(name = "comments")
@Getter
@EqualsAndHashCode(callSuper = true, of = {})
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
public class Comment extends QuaranoEntity<TrackedCase, CommentIdentifier> implements Comparable<Comment> {

	public static final Comparator<Comment> BY_DATE_DESCENDING = Comparator.comparing(Comment::getDate);

	private LocalDateTime date;
	private String text, author;

	public Comment(String text, String author) {

		Assert.hasText(text, "Comment text must not be null or empty!");

		this.id = CommentIdentifier.of(UUID.randomUUID());
		this.date = LocalDateTime.now();
		this.text = text;
		this.author = author;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	@SuppressWarnings("null")
	public int compareTo(Comment that) {
		return BY_DATE_DESCENDING.compare(this, that);
	}

	@Embeddable
	@EqualsAndHashCode
	@RequiredArgsConstructor(staticName = "of")
	@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
	public static class CommentIdentifier implements Identifier, Serializable {

		private static final long serialVersionUID = 7871473225101042167L;

		final UUID commentId;

		@Override
		public String toString() {
			return commentId.toString();
		}
	}
}
