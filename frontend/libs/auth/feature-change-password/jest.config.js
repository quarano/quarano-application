module.exports = {
  name: 'auth-feature-change-password',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/feature-change-password',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
