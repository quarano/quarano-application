import './commands';

/*
 * workaround for cypress electron issue.
 * For further information visit https://github.com/cypress-io/cypress/issues/2118#issuecomment-580095512
 */
Cypress.on('window:before:load', function (win) {
  // @ts-ignore
  const original = win.EventTarget.prototype.addEventListener;
  // @ts-ignore
  win.EventTarget.prototype.addEventListener = function () {
    if (arguments && arguments[0] === 'beforeunload') {
      return;
    }
    return original.apply(this, arguments);
  };

  Object.defineProperty(win, 'onbeforeunload', {
    get: function () {},
    set: function () {},
  });
});
