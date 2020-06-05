module.exports = {
  name: 'info-terms',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/info/terms',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
