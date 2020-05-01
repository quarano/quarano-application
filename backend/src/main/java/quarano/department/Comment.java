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

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.jddd.core.types.Identifier;
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

	private @Column(name = "comment_date") LocalDateTime date;
	
	private @Column(name = "comment_text") String comment;
	private String author;

	public Comment(String comment, String author) {

		Assert.hasText(comment, "Comment must not be null or empty!");

		this.id = CommentIdentifier.of(UUID.randomUUID());
		this.date = LocalDateTime.now();
		this.comment = comment;
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

		final @Column(name = "id") UUID commentId;

		@Override
		public String toString() {
			return commentId.toString();
		}
	}
}
