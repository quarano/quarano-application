module.exports = {
  name: 'all-users-feature-terms',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/all-users/feature-terms',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
