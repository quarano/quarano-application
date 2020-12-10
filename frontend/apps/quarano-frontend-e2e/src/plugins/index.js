// ***********************************************************
// This example plugins/index.js can be used to load plugins
//
// You can change the location of this file or turn off loading
// the plugins file with the 'pluginsFile' configuration option.
//
// You can read more here:
// https://on.cypress.io/plugins-guide
// ***********************************************************

// This function is called when a project is opened or re-opened (e.g. due to
// the project's config changing)

const {preprocessTypescript} = require('@nrwl/cypress/plugins/preprocessor');
const clipboardy = require('clipboardy');

module.exports = (on, config) => {
  // `on` is used to hook into various events Cypress emits
  // `config` is the resolved Cypress config
  require('@cypress/code-coverage/task')(on, config);

  // Preprocess Typescript file using Nx helper
  on('file:preprocessor', preprocessTypescript(config));

  // set browser language to German to avoid test failures to due the developer's browser locale
  // https://docs.cypress.io/api/plugins/browser-launch-api.html#Modify-browser-launch-arguments-preferences-and-extensions
  on('before:browser:launch', (browser, launchOptions) => {
    if (browser.family === 'chromium' && browser.name !== 'electron') {
      launchOptions.preferences.default.intl = {accept_languages: 'de'};
      return launchOptions;
    }

    if (browser.family === 'firefox') {
      launchOptions.preferences['intl.locale.requested'] = 'de';
      return launchOptions;
    }
  });

  on('task', {
    getClipboard() {
      return clipboardy.readSync();
    }
  });

  return config;
};
