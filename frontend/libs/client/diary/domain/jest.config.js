module.exports = {
  name: 'client-diary-domain',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/diary/domain',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
