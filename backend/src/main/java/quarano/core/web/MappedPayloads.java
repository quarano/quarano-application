package quarano.core.web;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

/**
 * A monadic type to implement Spring MVC handler methods in a functional way.
 *
 * @author Oliver Drotbohm
 */
public interface MappedPayloads {

	/**
	 * Creates a new {@link MappedErrors} from the given {@link Errors} instance.
	 *
	 * @param errors must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static MappedErrors of(Errors errors) {
		return new MappedErrors(errors, MappedPayloads::toBadRequest);
	}

	/**
	 * Creates a new {@link MappedPayload} for the given source instance
	 *
	 * @param <T>
	 * @param payload must not be {@literal null}.
	 * @param errors
	 * @return
	 */
	public static <T> MappedPayload<T> of(T payload, Errors errors) {

		Assert.notNull(payload, "Payload must not be null!");

		return MappedPayload.of(payload, errors);
	}

	/**
	 * Creates a new {@link MappedPayload} of the given source and {@link Errors}. Of the source is empty, a
	 * {@link HttpStatus#NOT_FOUND} will be produced independent of the built up pipeline.
	 *
	 * @param <T>
	 * @param source must not be {@literal null}.
	 * @param errors must not be {@literal null}.
	 * @return
	 */
	public static <T> MappedPayload<T> of(Optional<T> source, Errors errors) {
		return MappedPayload.of(source.orElse(null), errors);
	}

	/**
	 * Syntactic sugar to easily create a {@link HttpStatus#BAD_REQUEST} response from an {@link Errors} instance in a
	 * {@link Stream} mapping step or the like.
	 *
	 * @param errors must not be {@literal null}.
	 * @return will never be {@literal null}.
	 */
	public static HttpEntity<?> toBadRequest(Errors errors) {
		return of(errors).toBadRequest();
	}

	@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
	public static class MappedErrors {

		private final @NonNull Errors errors;
		protected final Function<Errors, HttpEntity<?>> onErrors;

		/**
		 * Creates a new {@link MappedPayload} with the given payload and the current {@link Errors} instance.
		 *
		 * @param <T>
		 * @param payload must not be {@literal null}.
		 * @return will never be {@literal null}.
		 */
		public <T> MappedPayload<T> with(T payload) {
			return MappedPayload.of(payload, errors);
		}

		/**
		 * Allows to peek at the current {@link Errors} instance.
		 *
		 * @param errors must not be {@literal null}.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedErrors peekErrors(Consumer<Errors> errors) {

			Assert.notNull(errors, "Errors handler must not be null!");

			errors.accept(this.errors);

			return this;
		}

		/**
		 * Rejects the field of the given name with the given error code.
		 *
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedErrors rejectField(String field, String errorCode) {

			Assert.hasText(field, "Field name must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");

			errors.rejectValue(field, errorCode);

			return this;
		}

		/**
		 * Rejects the field with the given name with the given error code if the given condition is true.
		 *
		 * @param condition the condition under which to reject the given field.
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedErrors rejectField(boolean condition, String field, String errorCode) {

			Assert.hasText(field, "Field name must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");

			return condition ? rejectField(field, errorCode) : this;
		}

		/**
		 * Rejects the field with the given name with the given error code and default message.
		 *
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @param defaultMessage must not be {@literal null} or empty.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedErrors rejectField(String field, String errorCode, String defaultMessage) {

			Assert.hasText(field, "Field name must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");
			Assert.hasText(defaultMessage, "Default message must not be null or empty!");

			errors.rejectValue(field, errorCode, defaultMessage);

			return this;
		}

		/**
		 * Rejects the field with the given name with the given error code if the given condition is true.
		 *
		 * @param condition the condition under which to reject the given field.
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @param errorHandler and error handler to be registered in case the condition is {@literal true}.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedErrors rejectField(boolean condition, String field, String errorCode,
				Function<Errors, HttpEntity<?>> errorHandler) {

			Assert.hasText(field, "Field name must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");
			Assert.notNull(errorHandler, "Error handler must not be null!");

			if (condition) {
				rejectField(field, errorCode);
				return onErrors(errorHandler);
			}

			return this;
		}

		/**
		 * Unconditionally creates a {@link HttpStatus#BAD_REQUEST} with the current {@link Errors} as payload.
		 *
		 * @return will never be {@literal null}.
		 */
		public HttpEntity<?> toBadRequest() {
			return ResponseEntity.badRequest().body(errors);
		}

		/**
		 * Creates an {@link HttpEntity} if the {@link MappedPayload} is valid, i.e. the underlying {@link Errors} has not
		 * accumulated any errors through previous validations.
		 *
		 * @param response must not be {@literal null}.
		 * @return
		 */
		public HttpEntity<?> onValidGet(Supplier<HttpEntity<?>> response) {

			Assert.notNull(response, "Response supplier must not be null!");

			return errorsOrNone().orElseGet(response);
		}

		/**
		 * Registers a {@link Function} to eventually turn an {@link Errors} instance into an {@link HttpEntity}. Will only
		 * be used if the {@link Errors} have accumulated at least one error in the pipeline.
		 *
		 * @param callback must not be {@literal null}.
		 * @return
		 */
		public MappedErrors onErrors(Function<Errors, HttpEntity<?>> callback) {

			Assert.notNull(callback, "Callback must not be null!");

			return new MappedErrors(errors, callback);
		}

		public MappedErrors onErrors(Supplier<HttpEntity<?>> callback) {

			Assert.notNull(callback, "Callback must not be null!");

			return onErrors(__ -> callback.get());
		}

		protected Optional<HttpEntity<?>> errorsOrNone() {

			return errors.hasErrors()
					? Optional.of(onErrors.apply(errors))
					: Optional.empty();
		}
	}

	/**
	 * A monadic type to work with a mapped payload alongside {@link Errors} to build up pipelines to eventually result in
	 * an {@link HttpEntity} based on the processing steps.
	 *
	 * @author Oliver Drotbohm
	 */
	public static class MappedPayload<T> extends MappedErrors {

		private final Errors errors;
		private final @Nullable T payload;
		private final Supplier<HttpEntity<?>> onAbsence;

		/**
		 * Creates a new {@link MappedPayload} with the given payload and {@link Errors}.
		 *
		 * @param <T>
		 * @param payload can be {@literal null}.
		 * @param errors must not be {@literal null}.
		 * @return will never be {@literal null}.
		 */
		private static <T> MappedPayload<T> of(@Nullable T payload, Errors errors) {

			Assert.notNull(errors, "Errors must not be null!");

			return new MappedPayload<>(payload, errors,
					it -> ResponseEntity.badRequest().body(it),
					() -> ResponseEntity.notFound().build());
		}

		private MappedPayload(@Nullable T payload, Errors errors, Function<Errors, HttpEntity<?>> onErrors,
				Supplier<HttpEntity<?>> onAbsence) {

			super(errors, onErrors);

			this.errors = errors;
			this.payload = payload;
			this.onAbsence = onAbsence;
		}

		/**
		 * Switches the {@link MappedPayload} to unconditionally produce an {@link HttpEntity} with status code
		 * {@link HttpStatus#NOT_FOUND}. Customize the eventual creation of that using {@link #onAbsence(Supplier)}.
		 *
		 * @param guard whether to end up with {@link HttpStatus#NOT_FOUND} or not.
		 * @return will never be {@literal null}.
		 * @see #onAbsence(Supplier)
		 */
		public MappedPayload<T> notFoundIf(boolean guard) {
			return guard ? withoutPayload() : this;
		}

		/**
		 * Truns the {@link MappedPayload} to unconditionally produce an {@link HttpEntity} with status code
		 * {@link HttpStatus#NOT_FOUND} if the given predicate on the payload matches. Customize the eventual creation of
		 * that using {@link #onAbsence(Supplier)}.
		 *
		 * @param predicate must not be {@literal null}.
		 * @return will never be {@literal null}.
		 * @see #onAbsence(Supplier)
		 */
		public MappedPayload<T> notFoundIf(Predicate<? super T> predicate) {

			Assert.notNull(predicate, "Predicate must not be null!");

			return payload == null || predicate.test(payload) ? withoutPayload() : this;
		}

		/**
		 * Peeks at the payload if present and no errors have been accumulated or we're on a path to a
		 * {@link HttpStatus#NOT_FOUND} yet.
		 *
		 * @param consumer must not be {@literal null}.
		 * @return the current instance, never {@literal null}.
		 */
		public MappedPayload<T> peek(Consumer<? super T> consumer) {

			Assert.notNull(consumer, "Consumer must not be null!");

			if (!errors.hasErrors() && payload != null) {
				consumer.accept(payload);
			}

			return this;
		}

		/**
		 * Peeks at the given payload and {@link Errors} if no errors have been accumulated or we're on a path to a
		 * {@link HttpStatus#NOT_FOUND} yet.
		 *
		 * @param consumer
		 * @return the current instance, never {@literal null}.
		 */
		public MappedPayload<T> peek(BiConsumer<? super T, Errors> consumer) {

			Assert.notNull(consumer, "Consumer must not be null!");

			if (!errors.hasErrors() && payload != null) {
				consumer.accept(payload, errors);
			}

			return this;
		}

		public MappedPayload<T> alwaysPeek(BiConsumer<? super T, Errors> consumer) {

			Assert.notNull(consumer, "Consumer must not be null!");

			if (payload != null) {
				consumer.accept(payload, errors);
			}

			return this;
		}

		/**
		 * Applies the given {@link Function}, even if errors have been accumulated.
		 *
		 * @param <S>
		 * @param mapper must not be {@literal null}.
		 * @return will never be {@literal null}.
		 */
		@SuppressWarnings("unchecked")
		public <S> MappedPayload<S> alwaysMap(Function<? super T, S> mapper) {

			Assert.notNull(mapper, "Mapper must not be null!");

			return payload == null ? (MappedPayload<S>) this : withPayload(mapper.apply(payload));
		}

		@SuppressWarnings("unchecked")
		public <S> MappedPayload<S> alwaysFlatMap(Function<? super T, Optional<S>> mapper) {

			Assert.notNull(mapper, "Mapper must not be null!");

			return payload == null
					? (MappedPayload<S>) this
					: mapper.apply(payload).map(this::withPayload).orElseGet(this::withoutPayload);
		}

		/**
		 * Applies the given {@link Function} if no errors have been accumulated or we're on a path to a
		 * {@link HttpStatus#NOT_FOUND} yet.
		 *
		 * @param <S>
		 * @param mapper must not be {@literal null}.
		 * @return
		 */
		@SuppressWarnings("unchecked")
		public <S> MappedPayload<S> map(Function<? super T, S> mapper) {

			Assert.notNull(mapper, "Mapper must not be null!");

			return errors.hasErrors() || payload == null
					? (MappedPayload<S>) this
					: withPayload(mapper.apply(payload));
		}

		@SuppressWarnings("unchecked")
		public <S> MappedPayload<S> flatMap(Function<? super T, Optional<S>> mapper) {

			Assert.notNull(mapper, "Mapper must not be null!");

			return errors.hasErrors() || payload == null
					? (MappedPayload<S>) this
					: mapper.apply(payload)
							.map(this::withPayload)
							.orElseGet(this::withoutPayload);
		}

		public <S> MappedPayload<S> alwaysMap(BiFunction<? super T, Errors, S> mapper) {
			return withPayload(mapper.apply(payload, errors));
		}

		@SuppressWarnings("unchecked")
		public <S> MappedPayload<S> map(BiFunction<? super T, Errors, S> mapper) {

			return errors.hasErrors() || payload == null
					? (MappedPayload<S>) this
					: withPayload(mapper.apply(payload, errors));
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#onErrors(java.util.function.Function)
		 */
		@Override
		public MappedPayload<T> onErrors(Function<Errors, HttpEntity<?>> callback) {

			Assert.notNull(callback, "Callback must not be null!");

			return new MappedPayload<>(payload, errors, callback, onAbsence);
		}

		/*
		 *
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#onErrors(java.util.function.Supplier)
		 */
		@Override
		public MappedPayload<T> onErrors(Supplier<HttpEntity<?>> callback) {

			Assert.notNull(callback, "Callback must not be null!");

			return onErrors(__ -> callback.get());
		}

		/**
		 * Registers a {@link Function} to create an {@link HttpEntity} in case the pipeline yields the absence of a
		 * payload.
		 *
		 * @param callback must not be {@literal null}.
		 * @return
		 * @see #notFoundIf(boolean)
		 * @see #notFoundIf(Predicate)
		 */
		public MappedPayload<T> onAbsence(Supplier<HttpEntity<?>> callback) {

			Assert.notNull(callback, "Callback must not be null!");

			return new MappedPayload<>(payload, errors, onErrors, callback);
		}

		/**
		 * Registers the given field to be rejected on payload absence. Transparently registers the error handler as absence
		 * handler.
		 *
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @return will never be {@literal null}.
		 * @since 1.4
		 */
		public MappedPayload<T> onAbsenceReject(String field, String errorCode) {

			Assert.hasText(field, "Field to reject must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");

			errors.rejectValue(field, errorCode);

			return new MappedPayload<T>(payload, errors, onErrors, () -> onErrors.apply(errors));
		}

		/**
		 * Concludes the processing of the current payload with the given finalizer, but automatically produces an error
		 * response via the callbacks registered for {@link #onErrors} and {@link #onAbsence}.
		 *
		 * @param finalizer must not be {@literal null}.
		 * @return
		 */
		public HttpEntity<?> concludeIfValid(Function<? super T, ? extends HttpEntity<?>> finalizer) {

			Assert.notNull(finalizer, "Finalizer must not be null!");

			return errorsOrNone().orElseGet(() -> finalizer.apply(payload));
		}

		/**
		 * Concludes the processing of the current payload with the given finalizer, but automatically produces an error
		 * response via the callbacks registered for {@link #onErrors} and {@link #onAbsence}.
		 *
		 * @param finalizer must not be {@literal null}.
		 * @return
		 */
		public HttpEntity<?> concludeIfValid(BiFunction<? super T, Errors, ? extends HttpEntity<?>> finalizer) {

			Assert.notNull(finalizer, "Finalizer must not be null!");

			return errorsOrNone().orElseGet(() -> finalizer.apply(payload, errors));
		}

		/**
		 * Concludes the processing of the current payload with the given finalizer, but automatically produces an error
		 * response via the callbacks registered for {@link #onErrors} and {@link #onAbsence}.
		 *
		 * @param finalizer must not be {@literal null}.
		 * @return
		 */
		public HttpEntity<?> concludeSelfIfValid(BiFunction<? super T, MappedErrors, ? extends HttpEntity<?>> finalizer) {

			Assert.notNull(finalizer, "Finalizer must not be null!");

			return errorsOrNone().orElseGet(() -> finalizer.apply(payload, this));
		}

		/**
		 * Terminal operation that produces an empty response with {@link HttpStatus#NO_CONTENT}.
		 *
		 * @return
		 * @since 1.4
		 */
		public HttpEntity<?> concludeWithoutContent() {
			return concludeIfValid(__ -> ResponseEntity.noContent().build());
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#rejectField(boolean, java.lang.String, java.lang.String)
		 */
		@Override
		public MappedPayload<T> rejectField(boolean condition, String field, String errorCode) {

			super.rejectField(condition, field, errorCode);

			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#rejectField(java.lang.String, java.lang.String)
		 */
		@Override
		public MappedPayload<T> rejectField(String field, String errorCode) {

			super.rejectField(field, errorCode);

			return this;
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#rejectField(java.lang.String, java.lang.String, java.lang.String)
		 */
		@Override
		public MappedErrors rejectField(String field, String errorCode, String defaultMessage) {

			super.rejectField(field, errorCode, defaultMessage);

			return this;
		}

		/**
		 * Rejects the field with the given name with the given error code if the given condition is true.
		 *
		 * @param condition the condition under which to reject the given field.
		 * @param field must not be {@literal null} or empty.
		 * @param errorCode must not be {@literal null} or empty.
		 * @param errorHandler and error handler to be registered in case the condition is {@literal true}.
		 * @return the current instance, never {@literal null}.
		 */
		@Override
		public MappedPayload<T> rejectField(boolean condition, String field, String errorCode,
				Function<Errors, HttpEntity<?>> errorHandler) {

			Assert.hasText(field, "Field name must not be null or empty!");
			Assert.hasText(errorCode, "Error code must not be null or empty!");
			Assert.notNull(errorHandler, "Error handler must not be null!");

			if (condition) {
				rejectField(field, errorCode);
				return onErrors(errorHandler);
			}

			return this;
		}

		private <S> MappedPayload<S> withoutPayload() {
			return new MappedPayload<S>(null, errors, onErrors, onAbsence);
		}

		private <S> MappedPayload<S> withPayload(S payload) {
			return new MappedPayload<S>(payload, errors, onErrors, onAbsence);
		}

		/*
		 * (non-Javadoc)
		 * @see quarano.core.web.MappedPayloads.MappedErrors#errorsOrNone()
		 */
		@Override
		protected Optional<HttpEntity<?>> errorsOrNone() {

			Optional<HttpEntity<?>> byPayload = payload == null
					? Optional.of(onAbsence.get())
					: Optional.empty();

			return byPayload.or(super::errorsOrNone);
		}
	}
}
