module.exports = {
  name: 'administration-accounts-account-detail',
  preset: '../../../../jest.config.js',
  coverageDirectory:
    '../../../../coverage/libs/administration/accounts/account-detail',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
