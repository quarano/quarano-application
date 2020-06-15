module.exports = {
  name: 'administration-api',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/administration/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
