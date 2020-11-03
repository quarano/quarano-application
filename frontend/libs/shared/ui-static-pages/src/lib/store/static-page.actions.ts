import { StaticPageDto } from './../model/static-page';
import { createAction, props } from '@ngrx/store';

export const loadStaticPages = createAction('[Static Pages] Load');

export const staticPagesLoaded = createAction('[Static Pages] Loaded', props<{ staticPages: StaticPageDto[] }>());
