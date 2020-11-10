module.exports = {
  name: 'shared-util-date',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-date',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
