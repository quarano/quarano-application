module.exports = {
  name: 'client-diary-diary-detail',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/diary/diary-detail',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
