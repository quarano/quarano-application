module.exports = {
  name: 'shared-util-data-access',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-data-access',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
