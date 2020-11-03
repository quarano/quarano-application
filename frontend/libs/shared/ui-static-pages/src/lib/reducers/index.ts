import { StaticPageDto } from './../model/static-page';
import { createReducer, on } from '@ngrx/store';
import { StaticPageActions } from '../store/action-types';

export interface StaticPageState {
  items: StaticPageDto[];
  loaded: boolean;
}

export const staticPageFeatureKey = 'staticPage';

export const initialStaticPageState: StaticPageState = {
  items: [],
  loaded: false,
};

export const staticPageReducer = createReducer(
  initialStaticPageState,

  on(StaticPageActions.loadStaticPages, (state) => ({
    ...state,
    loaded: false,
  })),

  on(StaticPageActions.staticPagesLoaded, (state, { staticPages }) => {
    return {
      ...state,
      loaded: true,
      items: staticPages,
    };
  })
);
