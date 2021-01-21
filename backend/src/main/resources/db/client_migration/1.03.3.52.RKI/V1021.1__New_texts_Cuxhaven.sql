-- new-contact-case de
UPDATE public.email_texts  SET text =
'Sehr geehrte/geehrter Frau/Herr {lastName},


Sie wurden von einem bestätigten COVID-19 Fall als Kontaktperson angegeben. Mit dieser Mail möchten wir Sie vorab informieren, dass das Gesundheitsamt Cuxhaven Kontakt zu Ihnen aufnehmen wird.

Sie erhalten in Kürze die Registrierungsdaten zu unserer Online-Anwendung zur Erfassung der Kontakte und Symptome.

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in die Online-Anwendung mit aufzunehmen.

Das weitere Vorgehen wird ein Mitarbeiter des Gesundheitsamtes persönlich mit Ihnen per Telefon besprechen. Bis dahin bitten wir Sie um Folgendes: 

• Bleiben Sie zu Hause und vermeiden Sie Kontakte zu anderen Personen.
• Sie und andere Personen in Ihrem Haushalt sollten regelmäßig, gründlich und mindestens 20 Sekunden lang die Hände mit Seife waschen. 
• Die Hände sollten aus dem Gesicht ferngehalten werden, insbesondere von Mund, Augen und Nase. 
• Beachten Sie die Husten- und Niesregeln: Halten Sie beim Husten oder Niesen größtmöglichen Abstand zu anderen und drehen Sie sich dabei am besten weg. Niesen oder husten Sie in die Armbeuge oder in ein Einwegtaschentuch, das Sie anschließend entsorgen. Waschen Sie danach und auch nach dem Naseputzen gründlich die Hände.

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de:

    • Montag bis Freitag von 8:00 – 16:00 Uhr
    • Samstags von 9:00 – 13:00 Uhr

Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. 
Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen!

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = 'a8968b23-6d5e-43e3-a084-3c0890fc61ff';

-- new-contact-case en
UPDATE public.email_texts  SET text =
'Dear Mr/Ms {lastName}


You have been named as a contact by a confirmed COVID-19 case. This message is to inform you in advance that the Gesundheitsamt Cuxhaven will be getting in touch with you.

You get immediately the information to register in our online-application for the recording of contacts and symptoms.

Please enter in this online-tool your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

An employee of the health authority will be discuss further procedure with you directly via telephone. Until then we kindly ask you to do the following:

• Stay at home and avoid contact with other people.
• You and all other members of your household should wash your hands with soap regularly and thoroughly for at least 20 seconds.
• Please keep your hands away from your face, particularly mouth, eyes and nose.
• Please follow coughing and sneezing etiquette: keep as far away from other people as possible whilst coughing or sneezing and turn away from them. Cough or sneeze into your elbow or use a disposable tissue, which should be disposed of immediately. Wash your hands thoroughly afterwards and also after blowing your nose.

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.

The city of Cuxhaven''s homepage (www.Landkreis-Cuxhaven.de) also offers a great deal of information.

If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.

Thank you for your cooperation.

Best wishes

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '037beef1-d182-4682-abbf-9ce81ea73ffe';

-- diary-reminder de
UPDATE public.email_texts  SET text = 
'Sehr geehrte/geehrter Frau/Herr {lastName},


bitte denken Sie an Ihren regelmäßigen Tagebucheintrag in der Quarano-Anwendung.
Uns fehlt noch der {slot}.

https://{host}

Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de:

    • Montag bis Freitag von 8:00 – 16:00 Uhr
    • Samstags von 9:00 – 13:00 Uhr


Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = 'aa2b4d73-2995-44aa-a639-f2f2837eb000';

-- diary-reminder en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName}


please remember to record your regular diary entry in the Quarano-Application.
We are missing data for {slot}.

https://{host}

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

Thank you for your cooperation.

Best wishes

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '331770d7-48b1-4bf3-bee9-8382918d8a43';

-- registration-contact de
UPDATE public.email_texts  SET text = 
'Sehr geehrte/geehrter Frau/Herr {lastName},


Sie wurden vom Gesundheitsamt Cuxhaven kontaktiert und als enge Kontaktperson zu einem bestätigten Covid-19 Fall eingestuft und es wurde eine Quarantäne angeordnet. 

Wie mit Ihnen am Telefon besprochen, melden Sie sich bitte umgehend in unserer Online-Anwendung zur Erfassung der Kontakte und Symptome an unter:

https://{host}/client/enrollment/landing/contact/{activationCode}

Bitte erfassen Sie dort Ihre Stammdaten und tragen Sie mögliche Kontaktpersonen ein. Zudem bitten wir Sie, täglich zweimal Fieber zu messen sowie auftretende Symptome (wie z.B. Atembeschwerden, Glieder- oder Muskelschmerzen, Durchfall) direkt in das Online-Tool mit aufzunehmen.

Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der 
Telefonnummer 04721 66-2006 oder via Email an gesundheitsamt@landkreis-cuxhaven.de .

•   Montag bis Freitag von 8:00 – 16:00 Uhr
•   Samstags von 9:00 – 13:00 Uhr 

Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen.

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = '5ddc2853-40b8-47f2-8a21-55f4041f5363';

-- registration-contact en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName}


You have been contacted by the Gesundheitsamt Cuxhaven, classified as a close contact of a confirmed Covid-19 case and ordered to stay in quarantine.

As discussed on the telephone, please register immediately in our online-application for the recording of contacts and symptoms under:

https://{host}/client/enrollment/landing/contact/{activationCode}

Please enter your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

If you have any questions, please do not hesitate to contact us. For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.


If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.

Thank you for your cooperation.

Best wishes

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

Auch die Internetseite des Landkreises Cuxhaven (www.landkreis-Cuxhaven.de) bietet eine Vielzahl von Informationen an.

Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell in Isolation stehen.

Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute. 

Mit freundlichen Grüßen
Ihr Gesundheitsamt Cuxhaven
' WHERE email_text_id = '94d0a37e-daed-465c-8bcb-ffdaca9062a3';

-- registration-index en
UPDATE public.email_texts  SET text = 
'Dear Mr/Ms {lastName}

As you already know, you are required to remain in quarantine until {quarantineEndDate}.

As discussed on the telephone, please register immediately in our online-application for the recording of contacts and symptoms under:

https://{host}/client/enrollment/landing/index/{activationCode}

Please enter your personal information and details of any potential contacts. Further we request that you take your temperature twice daily and record this together with any symptoms you might experience (e.g. difficulty breathing, body or muscle aches, diarrhoea, etc) directly in the online-tool.

For general questions regarding Corona please contact Cuxhaven''s Corona-Hotline under the phone number 04721 66-2006 or via Email to gesundheitsamt@landkreis-cuxhaven.de .

• Monday to Friday from 9 am – 4 pm
• Saturday from 9 am – 1 pm

In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.

The city of Cuxhaven''s homepage (www.Landkreis-Cuxhaven.de) also offers a great deal of information.


If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in isolation.

Thank you for your cooperation.

Best wishes

Your Gesundheitsamt Cuxhaven
' WHERE email_text_id = '58541fa0-c68a-4011-9b78-0b30a8406064';

-- welcome-contact de
UPDATE public.frontend_texts  SET text = 
'<section>
  <p>
    Sie haben vom Gesundheitsamt Cuxhaven einen personalisierten Registrierungs-Link erhalten. In den folgenden Seiten melden Sie sich bitte an und nutzen die Anwendung. Mit der Nutzung dieser Anwendung unterstützen Sie das Gesundheitsamt und tragen dazu bei, dass schneller agiert werden kann.
  </p>
  <p>
    Mit der Anwendung können Sie:
  </p>
  <ul>
    <li>Ihre Stammdaten aktualisieren</li>
    <li>Kontaktpersonen angeben</li>
    <li>Mögliche Krankheitszeichen protokollieren</li>
  </ul>
  <h3><strong>Infos zur Registrierung</strong></h3>
  <p>
    Registrieren Sie sich mit einem Benutzernamen und einem frei wählbaren Passwort. Danach melden Sie sich mit diesen Nutzerdaten an und können quarano nutzen.
  </p>
  <h3><strong>Infos zur Nutzung</strong></h3>
  <p>
    Ihre Stammdaten und mögliche, weitere Kontaktpersonen sind nur einmal einzugeben, mögliche Krankheitszeichen sind zweimal täglich einzugeben.
  </p>
  <p>
    <strong>Wichtig:</strong> Dieser Link ist nur für Sie persönlich gültig. Wenn mehrere betroffene Personen im selben Haushalt leben, ist es wichtig, dass sich jede Person mit einem eigenen Zugangslink vom Gesundheitsamt jeweils einen eigenen Benutzer anlegt. So können alle Einträge immer richtig zugeordnet werden.
  </p>
  <p>
    Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der
    <br />
    Telefonnummer <a href="tel:+494721662006" class="phone-link">04721 66-2006</a> oder via Email an <a href="mailto:gesundheitsamt@landkreis-cuxhaven.de" class="phone-link">gesundheitsamt@landkreis-cuxhaven.de</a>:
  </p>
  <ul>
    <li>Montag bis Freitag von 8:00 – 16:00 Uhr</li>
    <li>Samstags von 9:00 – 13:00 Uhr</li>
  </ul>
  <p>
    Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
    <br />
    Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.
  </p>
  <p>
    Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell unter Quarantäne stehen. 
  </p>
  <p>
    Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute.
  </p>
  <p>
    Mit freundlichen Grüßen,
    <br />
    Ihr Gesundheitsamt Cuxhaven
  </p>
</section>' WHERE frontend_text_id = 'a6d20591-660c-48e0-8620-4151164272f3';

-- welcome-index de
UPDATE public.frontend_texts  SET text = 
'<section>
  <p>
    Sie haben vom Gesundheitsamt Cuxhaven einen personalisierten Registrierungs-Link erhalten. In den folgenden Seiten melden Sie sich bitte an und nutzen die Anwendung. Mit der Nutzung dieser Anwendung unterstützen Sie das Gesundheitsamt und tragen dazu bei, dass schneller agiert werden kann.
  </p>
  <p>
    Mit der Anwendung können Sie:
  </p>
  <ul>
    <li>Ihre Stammdaten aktualisieren</li>
    <li>Kontaktpersonen angeben</li>
    <li>Mögliche Krankheitszeichen protokollieren</li>
  </ul>
  <h3><strong>Infos zur Registrierung</strong></h3>
  <p>
    Registrieren Sie sich mit einem Benutzernamen und einem frei wählbaren Passwort. Danach melden Sie sich mit diesen Nutzerdaten an und können quarano nutzen.
  </p>
  <h3><strong>Infos zur Nutzung</strong></h3>
  <p>
    Ihre Stammdaten und mögliche, weitere Kontaktpersonen sind nur einmal einzugeben, mögliche Krankheitszeichen sind zweimal täglich einzugeben.
  </p>
  <p>
    <strong>Wichtig:</strong> Dieser Link ist nur für Sie persönlich gültig. Wenn mehrere betroffene Personen im selben Haushalt leben, ist es wichtig, dass sich jede Person mit einem eigenen Zugangslink vom Gesundheitsamt jeweils einen eigenen Benutzer anlegt. So können alle Einträge immer richtig zugeordnet werden.
  </p>
  <p>
    Bei Fragen scheuen Sie sich bitte nicht, uns zu kontaktieren. Bei allgemeinen Fragen zum Thema Corona wenden Sie sich bitte an das Bürgertelefon des Landkreises Cuxhaven unter der
    <br />
    Telefonnummer <a href="tel:+494721662006" class="phone-link">04721 66-2006</a> oder via Email an <a href="mailto:gesundheitsamt@landkreis-cuxhaven.de" class="phone-link">gesundheitsamt@landkreis-cuxhaven.de</a>:
  </p>
  <ul>
    <li>Montag bis Freitag von 8:00 – 16:00 Uhr</li>
    <li>Samstags von 9:00 – 13:00 Uhr</li>
  </ul>
  <p>
    Die mit dieser Applikation verarbeiteten personenbezogenen Daten werden ausschließlich zum Zweck der Nachverfolgung von bestätigten Corona-Infizierungen oder -Verdachtsfälle erhoben. Grundlage der Verarbeitung bildet das Gesetz zur Verhütung und Bekämpfung von Infektionskrankheiten beim Menschen (Infektionsschutzgesetz - IfSG).
    <br />
    Weitere Hinweise finden Sie unter: https://www.quarano.de/datenschutz.html.
  </p>
  <p>
    Wenn Sie akut ärztliche Behandlung benötigen, wenden Sie sich an Ihren Hausarzt/Ihre Hausärztin. Nachts und am Wochenende stehen der hausärztliche Bereitschaftsdienst (116 117) oder in schwerwiegenden Fällen die Notaufnahmen der Kliniken als Anlaufstelle zur Verfügung. In akuten Notfällen zögern Sie bitte nicht, den Rettungsdienst unter der 112 zu rufen. Vor einer persönlichen Vorstellung beim Arzt oder im Krankenhaus muss die telefonische Ankündigung mit Hinweis auf COVID-19 erfolgen und dass Sie aktuell in Isolation stehen.
  </p>
  <p>
    Wir bedanken uns für Ihre Mitarbeit und wünschen Ihnen alles Gute.
  </p>
  <p>
    Mit freundlichen Grüßen,
    <br />
    Ihr Gesundheitsamt Cuxhaven
  </p>
</section>' WHERE frontend_text_id = '959b477e-2b29-4e2d-9a4f-f53f590c5adc';

-- welcome-contact en
UPDATE public.frontend_texts  SET text = 
'<section>
  <p>
    You have received a personalized registration link from the Gesundheitsamt Cuxhaven. On the following pages you can log in and use the application. By using the application you can support the Gesundheitsamt and do your part to ensure that fast action can be taken.
  </p>
  <p>
    With the application you can:
  </p>
  <ul>
    <li>Update your basic information</li>
    <li>Enter contacts</li>
    <li>Record any signs of illness</li>
  </ul>
  <h3><strong>Infos for registration</strong></h3>
  <p>
    Register with a user name and your choice of password. You can then use this information to log in and use quarano.
  </p>
  <h3><strong>Infos for use</strong></h3>
  <p>
    You only need to enter your basic personal information once. Contacts and possible symptoms should be recorded twice daily.
  </p>
  <p>
    <strong>Important:</strong> This link is for your personal use only. If several affected people are living together in one household, it is important that each person registers as a separate user with their own personalized access link from the health authority. In this way, all the information entered can be correlated accurately.
  </p>
  <p>
    If you have any questions, please do not hesitate to contact us. If you have general questions about Corona, please call Cuxhaven''s Corona-Hotline under the
    <br />
    phone number <a href="tel:+494721662006" class="phone-link">04721 66-2006</a> or via Email to <a href="mailto:gesundheitsamt@landkreis-cuxhaven.de" class="phone-link">gesundheitsamt@landkreis-cuxhaven.de</a>:
  </p>
  <ul>
    <li>Monday to Friday from 9 am – 4 pm</li>
    <li>Saturday from 9 am – 1 pm</li>
  </ul>
  <p>
    In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.
  </p>
  <p>
    If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in quarantine.
  </p>
  <p>
    Thank you for your cooperation.
  </p>
  <p>
    Best wishes
  </p>
  <p>
    Your Gesundheitsamt Cuxhaven
  </p>
</section>' WHERE frontend_text_id = 'de909807-45fd-4160-8bff-c42759c775d7';

-- welcome-index en
UPDATE public.frontend_texts  SET text = 
'<section>
  <p>
    You have received a personalized registration link from the Gesundheitsamt Cuxhaven. On the following pages you can log in and use the application. By using the application you can support the Gesundheitsamt and do your part to ensure that fast action can be taken.
  </p>
  <p>
    With the application you can:
  </p>
  <ul>
    <li>Update your basic information</li>
    <li>Enter contacts</li>
    <li>Record any signs of illness</li>
  </ul>
  <h3><strong>Infos for registration</strong></h3>
  <p>
    Register with a user name and your choice of password. You can then use this information to log in and use quarano.
  </p>
  <h3><strong>Infos for use</strong></h3>
  <p>
    You only need to enter your basic personal information once. Contacts and possible symptoms should be recorded twice daily.
  </p>
  <p>
    <strong>Important:</strong> This link is for your personal use only. If several affected people are living together in one household, it is important that each person registers as a separate user with their own personalized access link from the health authority. In this way, all the information entered can be correlated accurately.
  </p>
  <p>
    If you have any questions, please do not hesitate to contact us. If you have general questions about Corona, please call Cuxhaven''s Corona-Hotline under the
    <br />
    phone number <a href="tel:+494721662006" class="phone-link">04721 66-2006</a> or via Email to <a href="mailto:gesundheitsamt@landkreis-cuxhaven.de" class="phone-link">gesundheitsamt@landkreis-cuxhaven.de</a>:
  </p>
  <ul>
    <li>Monday to Friday from 9 am – 4 pm</li>
    <li>Saturday from 9 am – 1 pm</li>
  </ul>
  <p>
    In our privacy-policy you will find more Informations about data-protection: https://www.quarano.de/datenschutz.html.
  </p>
  <p>
    If you need urgent medical treatment, please contact your general practitioner (GP). During the night and at weekends please contact the GP on-call service (116 117) or in severe cases the emergency room at the clinics. In case of acute emergency do not hesitate to call the ambulance service under the number 112. Before going to see your GP or going to the hospital in person, you must first call and tell them you have COVID-19 and are currently in isolation.
  </p>
  <p>
    Thank you for your cooperation.
  </p>
  <p>
    Best wishes
  </p>
  <p>
    Your Gesundheitsamt Cuxhaven
  </p>
</section>' WHERE frontend_text_id = '1acc1dd5-8319-402d-bb38-50db22f8a429';
