module.exports = {
  name: 'client-ui-forgotten-contact-dialog',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/ui-forgotten-contact-dialog',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
