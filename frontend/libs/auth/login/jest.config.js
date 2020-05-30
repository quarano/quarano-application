module.exports = {
  name: 'auth-login',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/login',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
