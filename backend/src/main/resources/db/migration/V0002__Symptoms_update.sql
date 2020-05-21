INSERT INTO public.symptoms VALUES ('8687c622-d223-42fb-a93f-7a1c3677a4a6', true,  'Gefühl zu wenig / keine Luft zu bekommen');
INSERT INTO public.symptoms VALUES ('288f414e-63b3-4a7d-8752-1548e6615da5', true,  'Schmerzen beim Atmen');
INSERT INTO public.symptoms VALUES ('5397f942-95cb-4e7e-8146-8201cf5bf623', true, 'Allgemeine Schwäche');

UPDATE public.symptoms SET is_characteristic = true WHERE symptom_id = '33200776-964e-4a54-84fa-7215204590dd'; --  Durchfall wird charakteristisch
UPDATE public.symptoms SET is_characteristic = true WHERE symptom_id = '116e016a-54cf-40a6-aef8-5027abb931c8'; --  Halsschmerzen wird charakteristisch
UPDATE public.symptoms SET is_characteristic = true, name = 'Laufende Nase / Schnupfen' WHERE symptom_id = 'ced041b6-24c1-429c-9961-dfcdd92270ba'; 
UPDATE public.symptoms SET is_characteristic = true, name = 'Geruchs- / Geschmacksstörung' WHERE symptom_id = '5bb1524e-bccd-4549-8abe-fb92c483593a';

DELETE FROM public.symptoms WHERE symptom_id = 'd6fb718e-e9f3-4c47-887c-dafdd702d528'; -- Kurzatmigkeit löschen
