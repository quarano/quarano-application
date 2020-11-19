ALTER TABLE public.symptoms ADD is_suspicious_at_index bool NOT NULL DEFAULT false;
ALTER TABLE public.symptoms ADD is_suspicious_at_contact bool NOT NULL DEFAULT false;

UPDATE public.symptoms  SET is_suspicious_at_index = true WHERE symptom_id = '8687c622-d223-42fb-a93f-7a1c3677a4a6';
UPDATE public.symptoms  SET is_suspicious_at_contact = true WHERE symptom_id = '8687c622-d223-42fb-a93f-7a1c3677a4a6';
UPDATE public.symptoms  SET is_suspicious_at_index = true WHERE symptom_id = '288f414e-63b3-4a7d-8752-1548e6615da5';
UPDATE public.symptoms  SET is_suspicious_at_contact = true WHERE symptom_id = '288f414e-63b3-4a7d-8752-1548e6615da5';
UPDATE public.symptoms  SET is_suspicious_at_contact = true WHERE symptom_id = '5bb1524e-bccd-4549-8abe-fb92c483593a';
