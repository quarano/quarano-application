module.exports = {
  name: 'general-feature-imprint',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/general/feature-imprint',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
