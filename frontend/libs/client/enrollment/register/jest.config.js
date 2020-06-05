module.exports = {
  name: 'client-enrollment-register',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/enrollment/register',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
