module.exports = {
  name: 'auth-forbidden',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/forbidden',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
