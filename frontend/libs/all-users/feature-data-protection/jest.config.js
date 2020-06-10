module.exports = {
  name: 'all-users-feature-data-protection',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/all-users/feature-data-protection',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
