module.exports = {
  name: 'client-feature-enrollment',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/feature-enrollment',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
