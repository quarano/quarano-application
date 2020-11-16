DELETE FROM public.email_texts WHERE email_text_id = 'aa2b4d73-2995-44aa-a639-f2f2837eb000'; 
INSERT INTO public.email_texts VALUES ('aa2b4d73-2995-44aa-a639-f2f2837eb000', 'diary-reminder', 'de', 
'Sehr geehrte/geehrter Frau/Herr {lastName},


bitte denken Sie an Ihren regelmäßigen Tagebucheintrag in der Quarano-Anwendung.
Uns fehlt noch der {slot}.

https://{host}

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an die Corona-
Hotline der Stadt Mannheim unter der Telefonnummer 
0621-293 2253.

• Montag bis Freitag von 9:00 – 17:00 Uhr
• Samstags sowie an Sonn- und Feiertagen von 9:00 – 14:00 Uhr 

Wir bedanken uns für Ihre Mitarbeit.

Ihr Gesundheitsamt Mannheim
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = '331770d7-48b1-4bf3-bee9-8382918d8a43'; 
INSERT INTO public.email_texts VALUES ('331770d7-48b1-4bf3-bee9-8382918d8a43', 'diary-reminder', 'en', 
'Dear Mrs/Mr {lastName},


please remember to record your regular diary entry in the Quarano-Application.
We are missing data for {slot}.

https://{host}

For general questions regarding Corona please contact Mannheim''s Corona-Hotline under the phone number 0621-293 2253.

• Monday to Friday from 9 am – 5 pm
• Saturday, Sunday and public holidays from 9 am – 2 pm

Thank you for your cooperation.

Your Gesundheitsamt Mannheim
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = 'a8968b23-6d5e-43e3-a084-3c0890fc61ff'; 
INSERT INTO public.email_texts VALUES ('a8968b23-6d5e-43e3-a084-3c0890fc61ff', 'new-contact-case', 'de',
'Sehr geehrte/geehrter Frau/Herr {lastName},

Sie wurden von einem bestätigten COVID-19 Fall als Kontaktperson angegeben. Mit dieser Mail möchten wir Sie vorab informieren, dass das Gesundheitsamt Mannheim Kontakt zu Ihnen aufnehmen wird, Ihren Status als Kontaktperson klären wird und das weitere Vorgehen mit Ihnen besprechen wird. Bis dahin bitten wir Sie um Folgendes: 

•   Bleiben Sie zu Hause und vermeiden Sie Kontakte zu anderen Personen.
•   Sie und andere Personen in Ihrem Haushalt sollten regelmäßig, gründlich und mindestens 20 Sekunden lang die Hände mit Seife waschen. 
•   Die Hände sollten aus dem Gesicht ferngehalten werden, insbesondere von Mund, Augen und Nase. 
•   Beachten Sie die Husten- und Niesregeln: Halten Sie beim Husten oder Niesen größtmöglichen Abstand zu anderen und drehen Sie sich dabei am besten weg. Niesen oder husten Sie in die Armbeuge oder in ein Einwegtaschentuch, das Sie anschließend entsorgen. Waschen Sie danach und auch nach dem Naseputzen gründlich die Hände. 

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an die Corona-Hotline der Stadt Mannheim unter der Telefonnummer 0621-293 2253.

•   Montag bis Freitag von 9:00 – 17:00 Uhr
•   Samstags sowie an Sonn- und Feiertagen von 9:00 – 14:00 Uhr 


Wir bedanken uns für Ihre Mitarbeit und werden Kontakt mit Ihnen aufnehmen.

Ihr Gesundheitsamt Mannheim



Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen rufen Sie den Notruf: 112. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen.
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = '037beef1-d182-4682-abbf-9ce81ea73ffe'; 
INSERT INTO public.email_texts VALUES ('037beef1-d182-4682-abbf-9ce81ea73ffe', 'new-contact-case', 'en',
'Dear Mrs/Mr {lastName},

You have been named as a contact by a confirmed COVID-19 case. 
With this mail we want to inform you that the health agency Mannheim will be contacting you soon to clarify your contact status and discuss further proceeding.

Up to that time we ask you to:

• Stay at home and avoid contact with other people.
• You and others in your household should regularly and thoroughly wash their hands with soap for at least 20 seconds.
• Hands should be kept away from the face, especially from the mouth, eyes and nose.
• Observe the rules of coughing and sneezing: 
 Keep the greatest possible distance from others when you cough or sneeze and ideally turn away from others. 
 Sneeze or cough into your elbow, not your hands or into a disposable handkerchief that you discard afterwards. 
 Remember to immediately wash your hands after blowing your nose, coughing or sneezing.

For general questions in regards to Corona feel free to call the hotline of the city of Mannheim under 0621-293 2253.

• Monday through Friday from 9:00 – 17:00 Uhr
• Saturdays, Sundays and on public holidays from 9:00 – 14:00 Uhr 

We''d like to thank you for your cooperation and will contact you soon.

Your Mannheim Health Angency

In case you require urgent medical treatment please contact your regular doctor - at night or during weekends the emergency on call service (116 117) in german language can be contacted or in serious cases the emgency of the hospitals 
In critical situations please do not hesitate calling 112.
Please note that in case of personal appearance at your doctor or in a hospital you shall infrom the respective beforehand informing about your relation to COVID-19.
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = '5ddc2853-40b8-47f2-8a21-55f4041f5363'; 
INSERT INTO public.email_texts VALUES ('5ddc2853-40b8-47f2-8a21-55f4041f5363', 'registration-contact', 'de',
'Sehr geehrte/geehrter Frau/Herr {lastName},


Sie wurden vom Gesundheitsamt Mannheim kontaktiert und als enge Kontaktperson zu einem bestätigten Covid-19 Fall eingestuft und es wurde eine Quarantäne bis {quarantineEndDate} angeordnet. 

Wie mit Ihnen am Telefon besprochen, melden Sie sich bitte umgehend in unserer Online-Anwendung zur Erfassung der Kontakte und Symptome an unter:

https://{host}/client/enrollment/landing/contact/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in das Online-Tool mit aufzunehmen.

Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an die Corona-Hotline der Stadt Mannheim unter der Telefonnummer 0621-293 2253.

•   Montag bis Freitag von 9:00 – 17:00 Uhr
•   Samstags sowie an Sonn- und Feiertagen von 9:00 – 14:00 Uhr 

Bei Fragen, die Sie als Kontaktperson im Speziellen betreffen, wenden Sie sich bitte an die Hotline für Kontaktpersonen unter der Telefonnummer: 0621 -293 2212.

•   Montag bis Freitag von 8:30-13:30 Uhr


Im Anhang erhalten Sie außerdem Informationen und Unterlagen, die für Sie als Kontaktperson wichtig sind. 


Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Ihr Gesundheitsamt Mannheim


Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen.
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = 'cee04c8f-427c-414b-883c-213d99744b4d'; 
INSERT INTO public.email_texts VALUES ('cee04c8f-427c-414b-883c-213d99744b4d', 'registration-contact', 'en',
'Dear Mrs/Mr {lastName},

You have been contacted by the health agency and considered as a person with close contact to a confirmed COVID-19 case and a quarantine has been ordered until including {quarantineEndDate}.

As discussed on the phone, please immediately register in our online application in order to capturing your contacts and symptoms with the following link: 

https://{host}/client/enrollment/landing/contact/{activationCode}

Within the aplication please maintain your master data and name possible contacts you had.
In addition we ask you to check your body temperature twice a day and capture the results within the online tool together with any occuring symptoms (like for example breathing difficulties, aching limbs or muscles, diarrhea).

In case of questions do not hesitate contacting us.
For general questions in regards to Corona feel free to call the hotline of the city of Mannheim under 0621-293 2253.

• Monday through Friday from 9:00 – 17:00 Uhr
• Saturdays, Sundays and on public holidays from 9:00 – 14:00 Uhr 

In case of questions specifically in the context of contact persons please call the specific hotline of contact persons: 0621 -293 2212.

• Monday through Friday from 8:30 – 13:30 Uhr

Attached you find additional information and documents that are important for ou as a contact person.

We''d like to thank you for your cooperation and wish you all the best.

Your Mannheim Health Angency

In case you require urgent medical treatment please contact your regular doctor - at night or during weekends the emergency on call service (116 117) in german language can be contacted or in serious cases the emgency of the hospitals. In critical situations please do not hesitate calling 112. Please note that in case of personal appearance at your doctor or in a hospital you shall infrom the respective beforehand informing about your relation to COVID-19.
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = '94d0a37e-daed-465c-8bcb-ffdaca9062a3'; 
INSERT INTO public.email_texts VALUES ('94d0a37e-daed-465c-8bcb-ffdaca9062a3', 'registration-index', 'de',
'Sehr geehrte/geehrter Frau/Herr {lastName},

Das Gesundheitsamt Mannheim hat Ihnen soeben mitgeteilt, dass Sie mit dem neuartigen Coronavirus SARS-CoV2 infiziert und somit ansteckungsfähig sind. Die Stadt Mannheim hat zur Regelung der Quarantäne eine Allgemeinverfügung erlassen. Diese ist ab dem 28.10.2020 gültig und kann unter folgendem Link eingesehen werden:
https://www.mannheim.de/de/informationen-zu-corona/aktuelle-rechtsvorschriften

Personen, die mit Ihnen im gleichen Haushalt wohnen und die bisher nicht selbst positiv getestet wurden, müssen eine Quarantäne von 14 Tagen ab dem Tag des ersten positiven Abstrichs in Ihrem Haushalt einhalten. Für weitere positiv getestete Personen in Ihrem Haushalt gelten die gleichen Quarantäneregeln wie für Sie, d.h. eine Quarantänedauer von mindestens 10 Tagen ab dem Tag des jeweiligen Abstrichs.

Nichteinhaltung dieser Quarantäneregeln kann dazu führen, dass dies als Ordnungswidrigkeit oder gegebenenfalls als Straftat verfolgt wird.
Das Gesundheitsamt ist verpflichtet, zu ermitteln, wo Sie sich infiziert haben könnten und wer durch Sie angesteckt worden sein könnte. 

Wie mit Ihnen am Telefon besprochen, melden Sie sich bitte umgehend in unserer Online-Anwendung zur Erfassung der Kontakte und Symptome an unter:

https://{host}/client/enrollment/landing/index/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein, so dass wir zu diesen Kontakt aufnehmen können


Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an die Corona-Hotline der Stadt Mannheim unter der Telefonnummer 0621-293 2253:

•   Montag bis Freitag von 9:00 – 17:00 Uhr
•   Samstags sowie an Sonn- und Feiertagen von 9:00 – 14:00 Uhr 


Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen rufen Sie den Notruf: 112. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen


Mit freundlichen Grüßen,
', NULL, NULL, NULL, NULL);

DELETE FROM public.email_texts WHERE email_text_id = '58541fa0-c68a-4011-9b78-0b30a8406064'; 
INSERT INTO public.email_texts VALUES ('58541fa0-c68a-4011-9b78-0b30a8406064', 'registration-index', 'en',
'Dear Mrs/Mr {lastName},

The health agency Mannheim has just informed you about the fact that you are infected with the new Corona virus SARS-CoV2 and as such you are infectious. The city of Mannheim has issued a general decree that regulates the quarantine. This is valid from 28-Oct-2020 and can be checked under the following link:
https://www.mannheim.de/de/informationen-zu-corona/aktuelle-rechtsvorschriften

People, that are living with you within the same household and that have up to now not been tested positive must adhere to a 14 days quaarantine starting with the day of the first positive test within the household. For additionally people within youtr household that are tested positive the same rules apply as for yourself, meaning a quarantine for at least 10 days staring with the day of the respective test.

The non-adherence to the quarantine rules can lead to persecution as adminstrative offense or possibly a criminal offense.
The health agency is obliged to determine where you could have been infected and which otehr people might have been infected by you.

As discussed on the phone, please immediately register in our online application for capturing of contacts and symptoms with the following link:

https://{host}/client/enrollment/landing/index/{activationCode}

Within the aplication please maintain your master data and name possible contacts you had, so that we can contact them as soon as possible to prevent further spread of the infection.

For general questions in regards to Corona feel free to call the hotline of the city of Mannheim under 0621-293 2253.

• Monday through Friday from 9:00 – 17:00 Uhr
• Saturdays, Sundays and on public holidays from 9:00 – 14:00 Uhr 

In case you require urgent medical treatment please contact your regular doctor - at night or during weekends the emergency on call service (116 117) in german language can be contacted or in serious cases the emgency of the hospitals. In critical situations please do not hesitate calling 112. Please note that in case of personal appearance at your doctor or in a hospital you shall infrom the respective beforehand informing about your relation to COVID-19 and an ongoing quarantine.

Best regards,
', NULL, NULL, NULL, NULL);
