import { ActionReducerMap, ActionReducer, MetaReducer } from '@ngrx/store';
import { environment } from '../../environments/environment';
import { routerReducer } from '@ngrx/router-store';
import { AuthActions } from '@qro/auth/api';

// tslint:disable-next-line: no-empty-interface
export interface AppState {}

export const reducers: ActionReducerMap<AppState> = {
  router: routerReducer,
};

export function logger(reducer: ActionReducer<any>): ActionReducer<any> {
  return (state, action) => {
    // console.log('state before: ', state);
    // console.log('action', action);

    return reducer(state, action);
  };
}

export function logoutMetaReducer(reducer: ActionReducer<any>): ActionReducer<any> {
  return (state, action) => {
    return reducer(action.type === AuthActions.logout.type ? {} : state, action);
  };
}

export const metaReducers: MetaReducer<AppState>[] = !environment.production
  ? [logger, logoutMetaReducer]
  : [logoutMetaReducer];
