module.exports = {
  name: 'client-enrollment-basic-data',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/enrollment/basic-data',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
