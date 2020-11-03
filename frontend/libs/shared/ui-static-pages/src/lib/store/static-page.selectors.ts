import { StaticPageDto, StaticPageKeys } from './../model/static-page';
import { staticPageFeatureKey, StaticPageState } from './../reducers/index';
import { createFeatureSelector, createSelector } from '@ngrx/store';

const selectStaticPageState = createFeatureSelector<StaticPageState>(staticPageFeatureKey);

export const selectStaticPages = createSelector(selectStaticPageState, (state) => state?.items);

export const areStaticPagesLoaded = createSelector(selectStaticPageState, (state) => state?.loaded);

export const selectStaticPageByKey = createSelector(
  selectStaticPages,
  (staticPages: StaticPageDto[], props: { key: StaticPageKeys }) => staticPages.find((l) => l.key === props.key)
);
