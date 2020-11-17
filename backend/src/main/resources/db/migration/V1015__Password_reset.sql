ALTER TABLE accounts ADD password_reset_expiry_date timestamp NULL;
ALTER TABLE accounts ADD password_reset_token uuid NULL;

INSERT INTO public.email_texts VALUES ('f465626d-c5d6-48d1-a06a-86fc8d4318e9', 'reset-password', 'de', 'Liebe(r) {fullName},
Sie haben einen Link zum Setzen eines neuen Passwortes angefordert. Mit dem unten stehenden Link können sie Ihr Passwort innerhalb der nächsten 12 Stunden neu vergeben.

{resetUrl}

Mit freundlichen Grüßen
Ihr quarano Team', NULL, NULL, NULL, NULL);
INSERT INTO public.email_texts VALUES ('38049eb9-fb54-4e29-8a47-46015b2f3aca', 'reset-password', 'en', 'Dear {fullName},
You have requested to reset your password. Follow the link below to enter a new password within the next 12 hours.

{resetUrl}

Sincerely,
your quarano Team', NULL, NULL, NULL, NULL);
