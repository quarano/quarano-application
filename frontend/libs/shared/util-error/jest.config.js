module.exports = {
  name: 'shared-util-error',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-error',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
