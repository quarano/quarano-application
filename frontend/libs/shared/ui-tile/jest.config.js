module.exports = {
  name: 'shared-ui-tile',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-tile',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
