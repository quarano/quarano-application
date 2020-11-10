module.exports = {
  name: 'shared-ui-confirmation-dialog',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-confirmation-dialog',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
