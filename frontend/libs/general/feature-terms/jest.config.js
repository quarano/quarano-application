module.exports = {
  name: 'general-feature-terms',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/general/feature-terms',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
