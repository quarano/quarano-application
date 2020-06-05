module.exports = {
  name: 'info-api',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/info/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
