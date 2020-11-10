module.exports = {
  name: 'shared-ui-aside',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-aside',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
