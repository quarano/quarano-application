module.exports = {
  name: 'administration-feature-account-list',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/administration/feature-account-list',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
