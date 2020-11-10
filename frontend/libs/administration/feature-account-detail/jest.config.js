module.exports = {
  name: 'administration-feature-account-detail',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/administration/feature-account-detail',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
