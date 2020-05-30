module.exports = {
  name: 'shared-util-progress-bar',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-progress-bar',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
