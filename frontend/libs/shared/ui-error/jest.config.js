module.exports = {
  name: 'shared-ui-error',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-error',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
