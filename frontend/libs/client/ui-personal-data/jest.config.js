module.exports = {
  name: 'client-ui-personal-data',
  preset: '../../../jest.config.js',
  coverageDirectory: '../../../coverage/libs/client/ui-personal-data',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
