module.exports = {
  name: 'client-feature-diary',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/feature-diary',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
