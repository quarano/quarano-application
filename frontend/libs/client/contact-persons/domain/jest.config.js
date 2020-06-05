module.exports = {
  name: 'client-contact-persons-domain',
  preset: '../../../../jest.config.js',
  coverageDirectory: '../../../../coverage/libs/client/contact-persons/domain',
  snapshotSerializers: [
    'jest-preset-angular/build/AngularNoNgAttributesSnapshotSerializer.js',
    'jest-preset-angular/build/AngularSnapshotSerializer.js',
    'jest-preset-angular/build/HTMLCommentSerializer.js',
  ],
};
