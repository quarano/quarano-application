module.exports = {
  name: 'auth-feature-login',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/feature-login',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
