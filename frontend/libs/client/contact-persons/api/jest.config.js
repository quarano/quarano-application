module.exports = {
  name: 'client-contact-persons-api',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/contact-persons/api',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
