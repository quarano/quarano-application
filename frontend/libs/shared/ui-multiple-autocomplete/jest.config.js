module.exports = {
  name: 'shared-ui-multiple-autocomplete',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/shared/ui-multiple-autocomplete',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
