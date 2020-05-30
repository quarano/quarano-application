module.exports = {
  name: 'client-enrollment-shell',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/enrollment/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
