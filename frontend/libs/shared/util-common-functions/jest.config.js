module.exports = {
  name: 'shared-util-common-functions',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-common-functions',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
