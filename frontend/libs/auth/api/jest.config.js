module.exports = {
  name: 'auth-api',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
