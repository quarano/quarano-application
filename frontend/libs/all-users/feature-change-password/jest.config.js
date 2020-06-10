module.exports = {
  name: 'all-users-feature-change-password',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/all-users/feature-change-password',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
