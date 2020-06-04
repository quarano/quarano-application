module.exports = {
  name: 'client-profile-profile',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/profile',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
