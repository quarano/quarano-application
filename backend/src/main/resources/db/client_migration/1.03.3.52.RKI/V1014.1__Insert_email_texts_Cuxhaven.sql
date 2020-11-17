-- diary-reminder de
UPDATE public.email_texts  SET text = 
'Sehr geehrte/geehrter Frau/Herr {lastName},

bitte denken Sie an Ihren regelmäßigen Tagebucheintrag in der Quarano-Anwendung.
Uns fehlt noch der {slot}.

https://{host}

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de:

•   Montag bis Freitag von 8:00 – 16:00 Uhr
•   Samstags von 9:00 – 13:00 Uhr

Wir bedanken uns für Ihre Mitarbeit.

Mit freundlichen Grüßen,
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = 'aa2b4d73-2995-44aa-a639-f2f2837eb000';

-- diary-reminder en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName},

please remember to record your regular diary entry in the Quarano-Application.
We are missing data for {slot}.

https://{host}

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

Thank you for your cooperation.

Best wishes,

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '331770d7-48b1-4bf3-bee9-8382918d8a43';

-- new-contact-case de
UPDATE public.email_texts  SET text =
'Sehr geehrte/geehrter Frau/Herr {lastName},

Sie wurden von einem bestätigten COVID-19 Fall als Kontaktperson angegeben. Mit dieser Mail möchten wir Sie vorab informieren, dass das Gesundheitsamt Cuxhaven Kontakt zu Ihnen aufnehmen wird.

Bitte melden Sie sich in der Zwischenzeit umgehend in unserer Online-Anwendung zur Erfassung der Kontakte und Symptome an unter:

https://{host}/client/enrollment/landing/contact/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in das Online-Tool mit aufzunehmen.

Das weitere Vorgehen wird ein Mitarbeiter des Gesundheitsamtes persönlich mit Ihnen per Telefon besprechen. Bis dahin bitten wir Sie um Folgendes: 

• Bleiben Sie zu Hause und vermeiden Sie Kontakte zu anderen Personen.
• Sie und andere Personen in Ihrem Haushalt sollten regelmäßig, gründlich und mindestens 20 Sekunden lang die Hände mit Seife waschen. 
• Die Hände sollten aus dem Gesicht ferngehalten werden, insbesondere von Mund, Augen und Nase. 
• Beachten Sie die Husten- und Niesregeln: Halten Sie beim Husten oder Niesen größtmöglichen Abstand zu anderen und drehen Sie sich dabei am besten weg. Niesen oder husten Sie in die Armbeuge oder in ein Einwegtaschentuch, das Sie anschließend entsorgen. Waschen Sie danach und auch nach dem Naseputzen gründlich die Hände.

Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de:

• Montag bis Freitag von 8:00 – 16:00 Uhr
• Samstags von 9:00 – 13:00 Uhr

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen. 

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen,
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = 'a8968b23-6d5e-43e3-a084-3c0890fc61ff';

-- new-contact-case en
UPDATE public.email_texts  SET text =
'Dear Mr/Ms {lastName},

You have been named as a contact by a confirmed COVID-19 case. This message is to inform you in advance that the Gesundheitsamt Cuxhaven will be getting in touch with you.

In the meantime, please register immediately in our online-application for the recording of contacts and symptoms under:

https://{host}/client/enrollment/landing/contact/{activationCode}

Please enter your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

An employee of the health authority will be discuss further procedure with you directly via telephone. Until then we kindly ask you to do the following:

• Stay at home and avoid contact with other people.
• You and all other members of your household should wash your hands with soap regularly and thoroughly for at least 20 seconds.
• Please keep your hands away from your face, particularly mouth, eyes and nose.
• Please follow coughing and sneezing etiquette: keep as far away from other people as possible whilst coughing or sneezing and turn away from them. Cough or sneeze into your elbow or use a disposable tissue, which should be disposed of immediately. Wash your hands thoroughly afterwards and also after blowing your nose.

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.

The city of Cuxhaven''s homepage (www.landkreis-cuxhaven.de) also offers a great deal of information.

If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.

Thank you for your cooperation.

Best wishes,

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '037beef1-d182-4682-abbf-9ce81ea73ffe';

-- registration-contact de
UPDATE public.email_texts  SET text = 
'Sehr geehrte/geehrter Frau/Herr {lastName},

Sie wurden vom Gesundheitsamt Cuxhaven kontaktiert und als enge Kontaktperson zu einem bestätigten Covid-19 Fall eingestuft und es wurde eine Quarantäne angeordnet. 

Wie mit Ihnen am Telefon besprochen, melden Sie sich bitte umgehend in unserer Online-Anwendung zur Erfassung der Kontakte und Symptome an unter:

https://{host}/client/enrollment/landing/contact/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in das Online-Tool mit aufzunehmen.

Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de:

•   Montag bis Freitag von 8:00 – 16:00 Uhr
•   Samstags von 9:00 – 13:00 Uhr 

Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen.

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen,
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = '5ddc2853-40b8-47f2-8a21-55f4041f5363';

-- registration-contact en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName},

You have been contacted by the Gesundheitsamt Cuxhaven, classified as a close contact of a confirmed Covid-19 case and ordered to stay in quarantine.

As discussed on the telephone, please register immediately in our online-application for the recording of contacts and symptoms under:

https://{host}/client/enrollment/landing/contact/{activationCode}

Please enter your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

If you have any questions, please do not hesitate to contact us. For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de.

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.

If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.

Thank you for your cooperation.

Best wishes,

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = 'cee04c8f-427c-414b-883c-213d99744b4d';

-- registration-index de
UPDATE public.email_texts  SET text = 
'Sehr geehrte/geehrter Frau/Herr {lastName},

wie Sie bereits wissen, befinden Sie sich bis einschließlich {quarantineEndDate} in Quarantäne.

Wie mit Ihnen am Telefon besprochen, melden Sie sich bitte umgehend in unserer Online-Anwendung zur Erfassung der Kontaktpersonen und der Symptome an unter:

https://{host}/client/enrollment/landing/index/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein, so dass wir zu diesem Kontakt aufnehmen können. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in das Online-Tool mit aufzunehmen.

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de: 

•   Montag bis Freitag von 8:00 – 16:00 Uhr
•   Samstags von 9:00 – 13:00 Uhr 

Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.

Auch die Internetseite des Landkreises Cuxhaven (www.landkreis-cuxhaven.de) bietet eine Vielzahl von Informationen an.

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen.

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen,
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = '94d0a37e-daed-465c-8bcb-ffdaca9062a3';

-- registration-index en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName},

As you already know, you are required to remain in quarantine until {quarantineEndDate}.

As discussed on the telephone, please register immediately in our online-application for the recording of contacts and symptoms under:

https://{host}/client/enrollment/landing/index/{activationCode}

Please enter your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de.

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.

The city of Cuxhaven''s homepage (www.landkreis-cuxhaven.de) also offers a great deal of information.

If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.

Thank you for your cooperation.

Best wishes,

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '58541fa0-c68a-4011-9b78-0b30a8406064';
