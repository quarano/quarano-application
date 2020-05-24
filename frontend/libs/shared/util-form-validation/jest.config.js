module.exports = {
  name: 'shared-util-form-validation',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/util-form-validation',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
