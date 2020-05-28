module.exports = {
  name: 'administration-accounts-shell',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/administration/accounts/shell',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
