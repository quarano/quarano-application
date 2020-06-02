module.exports = {
  name: 'info-imprint',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/info/imprint',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
