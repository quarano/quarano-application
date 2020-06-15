module.exports = {
  name: 'shared-ui-data-protection',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-data-protection',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
