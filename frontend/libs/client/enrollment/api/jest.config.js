module.exports = {
  name: 'client-enrollment-api',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/enrollment/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
