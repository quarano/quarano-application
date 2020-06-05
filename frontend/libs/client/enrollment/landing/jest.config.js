module.exports = {
  name: 'client-enrollment-landing',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/enrollment/landing',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
