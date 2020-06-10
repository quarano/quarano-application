module.exports = {
  name: 'all-users-feature-welcome',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/all-users/feature-welcome',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
