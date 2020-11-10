module.exports = {
  name: 'administration-shell',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/administration/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
