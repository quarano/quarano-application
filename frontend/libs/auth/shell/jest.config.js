module.exports = {
  name: 'auth-shell',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/auth/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
