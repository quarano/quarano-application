module.exports = {
  name: 'client-diary-shell',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/diary/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
